from __future__ import annotations

import argparse
import csv
import hashlib
import json
import os
import platform
import re
import shutil
import subprocess
import sys
import time
import uuid
from dataclasses import dataclass
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import pandas as pd
import yaml


ROOT = Path(__file__).resolve().parent
DEFAULT_MANIFEST = ROOT / "data_new" / "class_sampling_manifest_final_seed42.csv"
DEFAULT_RECIPES = ROOT / "data_new" / "build_recipes_portable.csv"
DEFAULT_COMPILED_REPOS = Path(os.getenv("RBL4_COMPILED_REPOS", ROOT.parent / "compiledrepos")).absolute()
DEFAULT_RESULTS_DIR = ROOT / "results" / "runs"
DEFAULT_MODEL = "gpt-4o-mini-2024-07-18"
DEFAULT_PROMPT = "rbl4-zero-shot"
AGONE_TEST_TYPES = ["evosuite", DEFAULT_MODEL]

REFERENCE = {
    "mutation_compiled_only_gpt4o_mini": 44.5,
    "branch_compiled_only_gpt4o_mini": 41.9,
    "build_success_gpt4o_mini": 0.286,
    "rq4_noninferiority_margin_pp": 5.0,
}

PROMPT_MESSAGES = [
    {
        "role": "system",
        "content": (
            "You are provided with Java class. Create a test class that fully tests "
            "the proposed Java class using the project information for imports. "
            "Reply with code only, do not add other text that is not code."
        ),
    },
    {
        "role": "user",
        "content": (
            "The project uses {{testing_framework}} and Java {{java_version}} and "
            "Java class is:\n<code>\n{{focal_class}}\n</code>"
        ),
    },
]


def utc_now() -> str:
    return datetime.now(timezone.utc).isoformat(timespec="seconds")


def safe_name(value: Any) -> str:
    return re.sub(r"[^A-Za-z0-9_.-]+", "_", str(value)).strip("_") or "item"


def prompt_hash() -> str:
    return hashlib.sha256(json.dumps(PROMPT_MESSAGES, sort_keys=True).encode("utf-8")).hexdigest()


def load_local_env() -> None:
    env_path = ROOT / ".env"
    if not env_path.exists():
        return
    for raw in env_path.read_text(encoding="utf-8").splitlines():
        line = raw.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        os.environ.setdefault(key.strip(), value.strip().strip('"').strip("'"))


def append_csv(path: Path, row: dict[str, Any], fieldnames: list[str] | None = None) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    names = fieldnames or list(row.keys())
    for attempt in range(10):
        try:
            exists = path.exists()
            with path.open("a", newline="", encoding="utf-8") as f:
                writer = csv.DictWriter(f, fieldnames=names, extrasaction="ignore")
                if not exists:
                    writer.writeheader()
                writer.writerow(row)
            return
        except PermissionError:
            time.sleep(0.1 * (attempt + 1))
    fallback = path.with_name(path.name + ".fallback.csv")
    exists = fallback.exists()
    with fallback.open("a", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=names, extrasaction="ignore")
        if not exists:
            writer.writeheader()
        writer.writerow(row)


def append_phase(run_dir: Path, phase: str, **kwargs: Any) -> None:
    started = kwargs.pop("started_at", None)
    detail = str(kwargs.pop("detail", ""))[:4000]
    row = {
        "timestamp_utc": utc_now(),
        "phase": phase,
        "project": kwargs.pop("project", ""),
        "module": kwargs.pop("module", ""),
        "arm": kwargs.pop("arm", ""),
        "sample_index": kwargs.pop("sample_index", ""),
        "class_key": kwargs.pop("class_key", ""),
        "focal_class": kwargs.pop("focal_class", ""),
        "test_class": kwargs.pop("test_class", ""),
        "status": kwargs.pop("status", ""),
        "duration_sec": round(time.time() - started, 3) if started is not None else "",
        "detail": detail,
    }
    append_csv(run_dir / "phase_log.csv", row)


def normalize_repo_rel_path(path: Any) -> str:
    value = str(path or "").strip().replace("\\", "/")
    value = value.removeprefix("./")
    while value.startswith("/"):
        value = value[1:]
    return value


def normalize_java_version(value: Any) -> str:
    raw = str(value or "").strip().lower()
    if raw in {"", "nan", "none", "unknown"}:
        return "1.8"
    if raw in {"5", "1.5"}:
        return "1.5"
    if raw in {"6", "1.6"}:
        return "1.6"
    if raw in {"7", "1.7"}:
        return "1.7"
    if raw in {"8", "1.8"}:
        return "1.8"
    return raw


def java_major(value: Any) -> int:
    version = normalize_java_version(value)
    if version.startswith("1."):
        version = version.split(".", 1)[1]
    match = re.search(r"\d+", version)
    return int(match.group(0)) if match else 8


def java_identifier(value: str) -> str:
    clean = re.sub(r"\W+", "_", str(value)).strip("_")
    if not clean:
        clean = "Generated"
    if not re.match(r"[A-Za-z_$]", clean[0]):
        clean = "_" + clean
    return clean


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8", errors="replace")


def java_package(source: str) -> str:
    cleaned = re.sub(r"//.*", "", source)
    cleaned = re.sub(r"/\*.*?\*/", "", cleaned, flags=re.DOTALL)
    match = re.search(r"\bpackage\s+([\w.]+)\s*;", cleaned)
    return match.group(1) if match else ""


def derive_test_path(focal_path: str, package_name: str, test_class: str) -> str:
    focal = normalize_repo_rel_path(focal_path)
    package_path = package_name.replace(".", "/") if package_name else ""
    if "/src/main/java/" in focal:
        prefix = focal.split("/src/main/java/", 1)[0]
        return f"{prefix}/src/test/java/{package_path}/{test_class}.java".replace("//", "/")
    if "/src/main/" in focal:
        prefix = focal.split("/src/main/", 1)[0]
        return f"{prefix}/src/test/java/{package_path}/{test_class}.java".replace("//", "/")
    parent = str(Path(focal).parent).replace("\\", "/")
    return f"{parent}/src/test/java/{package_path}/{test_class}.java".replace("//", "/")


def placeholder_test_source(package_name: str, test_class: str) -> str:
    package_line = f"package {package_name};\n\n" if package_name else ""
    return f"{package_line}public class {test_class} {{\n}}\n"


def infer_test_framework(repo_dir: Path, module_dir: str) -> dict[str, str | None]:
    search_dir = repo_dir / module_dir if module_dir else repo_dir
    texts: list[str] = []
    for name in ["pom.xml", "build.gradle", "build.gradle.kts"]:
        path = search_dir / name
        if path.exists():
            texts.append(read_text(path).lower())
    text = "\n".join(texts)
    if "testng" in text:
        return {"junit_version": None, "testng_version": "7"}
    if "junit-jupiter" in text or "org.junit.jupiter" in text:
        return {"junit_version": "5", "testng_version": None}
    return {"junit_version": "4", "testng_version": None}


@dataclass
class Dataset:
    manifest: pd.DataFrame
    recipes: pd.DataFrame
    sample: pd.DataFrame


def load_dataset(manifest_path: Path, recipes_path: Path, compiled_repos: Path) -> Dataset:
    manifest = pd.read_csv(manifest_path, dtype=str).fillna("")
    recipes = pd.read_csv(recipes_path, dtype=str).fillna("")
    required = {
        "repo_id",
        "class_key",
        "focal_class",
        "focal_path",
        "scope_key",
        "module_dir",
        "build_tool",
        "declared_java_version",
        "effective_java_runtime",
        "selection_hash",
    }
    missing = sorted(required - set(manifest.columns))
    if missing:
        raise ValueError(f"{manifest_path} missing columns: {missing}")
    recipe_required = {"repo_id", "scope_key", "portable_command_windows", "portable_command_posix"}
    recipe_missing = sorted(recipe_required - set(recipes.columns))
    if recipe_missing:
        raise ValueError(f"{recipes_path} missing columns: {recipe_missing}")

    recipe_index = {
        (str(row.repo_id), str(row.scope_key)): row._asdict()
        for row in recipes.itertuples(index=False)
    }
    rows: list[dict[str, Any]] = []
    for idx, row in manifest.iterrows():
        repo_id = str(row["repo_id"])
        focal_path = normalize_repo_rel_path(row["focal_path"])
        source_path = compiled_repos / repo_id / focal_path
        package_name = java_package(read_text(source_path)) if source_path.exists() else ""
        short_hash = hashlib.sha1(str(row["class_key"]).encode("utf-8")).hexdigest()[:8]
        test_class = f"{java_identifier(row['focal_class'])}_RBL4Test_{short_hash}"
        test_path = derive_test_path(focal_path, package_name, test_class)
        module = normalize_repo_rel_path(row["module_dir"])
        if module in {"", "."}:
            module = ""
        build_tool = str(row["build_tool"]).strip().lower()
        recipe = recipe_index.get((repo_id, str(row["scope_key"])), {})
        record = row.to_dict()
        record.update(
            {
                "sample_index": idx,
                "Project": repo_id,
                "Focal_Class": str(row["focal_class"]),
                "Test_Class": test_class,
                "Focal_Path": f"repos/{repo_id}/{focal_path}",
                "Test_Path": f"repos/{repo_id}/{test_path}",
                "Module": module,
                "Repo_Dir": str(compiled_repos / repo_id),
                "Compiled_Focal_Path": str(source_path),
                "Synthesized_Test_Rel_Path": test_path,
                "Package": package_name,
                "Java_Version": normalize_java_version(row["declared_java_version"]),
                "Build_Tool": "Gradle" if build_tool == "gradle" else "Maven",
                "Recipe_Command_Windows": recipe.get("portable_command_windows", ""),
                "Recipe_Command_Posix": recipe.get("portable_command_posix", ""),
                "Recipe_ID": recipe.get("recipe_id", ""),
            }
        )
        rows.append(record)
    sample = pd.DataFrame(rows)
    return Dataset(manifest=manifest, recipes=recipes, sample=sample)


def preflight(dataset: Dataset, compiled_repos: Path, run_dir: Path) -> tuple[pd.DataFrame, dict[str, Any]]:
    sample = dataset.sample.copy()
    sample["repo_exists"] = sample["Project"].map(lambda repo: int((compiled_repos / str(repo)).is_dir()))
    sample["focal_exists"] = sample["Compiled_Focal_Path"].map(lambda p: int(Path(str(p)).exists()))
    sample["recipe_exists"] = sample.apply(lambda row: int(bool(row["Recipe_Command_Windows"] or row["Recipe_Command_Posix"])), axis=1)
    sample["duplicate_class_key"] = sample.duplicated("class_key", keep=False).astype(int)
    sample["duplicate_focal_path"] = sample.duplicated(["Project", "focal_path"], keep=False).astype(int)
    sample["preflight_status"] = sample.apply(
        lambda row: "PASS"
        if row["repo_exists"] and row["focal_exists"] and row["recipe_exists"] and not row["duplicate_class_key"] and not row["duplicate_focal_path"]
        else "FAIL",
        axis=1,
    )
    scope_pairs = dataset.manifest[["repo_id", "scope_key"]].drop_duplicates()
    recipe_pairs = dataset.recipes[["repo_id", "scope_key"]].drop_duplicates()
    missing_scopes = (
        scope_pairs.merge(recipe_pairs, on=["repo_id", "scope_key"], how="left", indicator=True)
        .query("_merge == 'left_only'")[["repo_id", "scope_key"]]
        .to_dict(orient="records")
    )
    report = {
        "timestamp_utc": utc_now(),
        "manifest_rows": int(len(dataset.manifest)),
        "manifest_repos": int(dataset.manifest["repo_id"].nunique()),
        "recipe_rows": int(len(dataset.recipes)),
        "recipe_scopes": int(recipe_pairs.shape[0]),
        "classes_per_repo_min": int(dataset.manifest.groupby("repo_id").size().min()),
        "classes_per_repo_max": int(dataset.manifest.groupby("repo_id").size().max()),
        "compiledrepos_root": str(compiled_repos),
        "repo_missing_n": int((sample["repo_exists"] == 0).sum()),
        "focal_missing_n": int((sample["focal_exists"] == 0).sum()),
        "recipe_missing_n": int((sample["recipe_exists"] == 0).sum()),
        "preflight_failed_class_n": int((sample["preflight_status"] != "PASS").sum()),
        "duplicate_class_key_rows": int(sample["duplicate_class_key"].sum()),
        "duplicate_focal_path_rows": int(sample["duplicate_focal_path"].sum()),
        "missing_scope_recipes": missing_scopes,
        "prompt_hash_sha256": prompt_hash(),
        "prompt_protocol": "AgoneTest base zero-shot prompt from proposal; no java_language_rules; no project_structure/dependencies.",
    }
    sample.to_csv(run_dir / "preflight_classes.csv", index=False)
    (run_dir / "preflight_report.json").write_text(json.dumps(report, indent=2, ensure_ascii=False), encoding="utf-8")
    return sample, report


def command_for_recipe(row: pd.Series) -> str:
    if platform.system() == "Windows":
        return str(row.get("Recipe_Command_Windows", "")).strip()
    return str(row.get("Recipe_Command_Posix", "")).strip()


def load_toolchains():
    sys.path.insert(0, str((ROOT / "AgoneTest").resolve()))
    import toolchains  # type: ignore

    return toolchains


def run_baseline_scopes(sample: pd.DataFrame, run_dir: Path) -> tuple[pd.DataFrame, pd.DataFrame]:
    toolchains = load_toolchains()
    rows: list[dict[str, Any]] = []
    grouped = sample.groupby(["Project", "scope_key"], dropna=False)
    for (repo_id, scope_key), group in grouped:
        first = group.iloc[0]
        started = time.time()
        command = command_for_recipe(first)
        repo_dir = Path(str(first["Repo_Dir"]))
        module = str(first.get("Module", ""))
        status = "FAIL"
        detail = ""
        if not repo_dir.exists():
            detail = f"missing repo: {repo_dir}"
        elif not command:
            detail = "missing portable build command"
        else:
            command = command.replace("${REPO_DIR}", str(repo_dir))
            env = toolchains.build_java_env(first.get("Java_Version"), min_major=8)
            try:
                result = subprocess.run(
                    command,
                    cwd=repo_dir,
                    shell=True,
                    capture_output=True,
                    text=True,
                    timeout=int(os.getenv("RBL4_BASELINE_TIMEOUT_SECONDS", "900")),
                    env=env,
                )
                status = "PASS" if result.returncode == 0 else "FAIL"
                detail = ((result.stdout or "") + "\n" + (result.stderr or "")).strip()[-5000:]
            except subprocess.TimeoutExpired as exc:
                status = "FAIL"
                out = exc.stdout.decode("utf-8", errors="replace") if isinstance(exc.stdout, bytes) else (exc.stdout or "")
                err = exc.stderr.decode("utf-8", errors="replace") if isinstance(exc.stderr, bytes) else (exc.stderr or "")
                detail = f"timeout\n{out}\n{err}"[-5000:]
        row = {
            "project": repo_id,
            "scope_key": scope_key,
            "module": module,
            "class_rows": int(len(group)),
            "focal_classes": ";".join(group["Focal_Class"].astype(str).tolist()),
            "build_tool": first.get("Build_Tool", ""),
            "java_version": first.get("Java_Version", ""),
            "command": command,
            "status": status,
            "duration_sec": round(time.time() - started, 3),
            "detail": detail,
        }
        rows.append(row)
        append_phase(run_dir, "baseline_scope_build", project=repo_id, module=module, status=status, started_at=started, detail=detail or command)
    scope_df = pd.DataFrame(rows)
    status_by_scope = {
        (str(row.project), str(row.scope_key)): str(row.status)
        for row in scope_df.itertuples(index=False)
    }
    class_rows = []
    for _, row in sample.iterrows():
        status = status_by_scope.get((str(row["Project"]), str(row["scope_key"])), "FAIL")
        rec = row.to_dict()
        rec["baseline_build_status"] = status
        class_rows.append(rec)
    class_df = pd.DataFrame(class_rows)
    scope_df.to_csv(run_dir / "baseline_scope_build.csv", index=False)
    class_df.to_csv(run_dir / "baseline_classes.csv", index=False)
    return scope_df, class_df


def copy_repo_to_sandbox(source_repo: Path, target_repo: Path) -> None:
    ignore_names = {
        ".git",
        ".gradle",
        ".evosuite",
        "evosuite-tests",
        "evosuite-report",
        "target",
        "build",
    }

    def ignore(_dir: str, names: list[str]) -> set[str]:
        return {name for name in names if name in ignore_names}

    shutil.copytree(source_repo, target_repo, ignore=ignore)


def write_run_settings(model: str, prompt_name: str) -> None:
    settings = {
        "agents": [
            {"model": "evosuite"},
            {
                "model": model,
                "temperature": 0,
                "top_p": 1,
                "max_tokens": 2048,
                "frequency_penalty": 0,
                "presence_penalty": 0,
            },
        ],
        "prompts": [{"name": prompt_name, "value": PROMPT_MESSAGES}],
    }
    with (ROOT / "AgoneTest" / "run_settings.yaml").open("w", encoding="utf-8") as f:
        yaml.safe_dump(settings, f, sort_keys=False, allow_unicode=False)
    (ROOT / "prompt_manifest.json").write_text(
        json.dumps(
            {
                "timestamp_utc": utc_now(),
                "prompt_name": prompt_name,
                "model": model,
                "prompt_hash_sha256": prompt_hash(),
                "messages": PROMPT_MESSAGES,
                "protocol_note": "AgoneTest base zero-shot prompt from proposal; source-level rule placeholder removed.",
            },
            indent=2,
            ensure_ascii=False,
        ),
        encoding="utf-8",
    )


def project_info_for_row(row: pd.Series, sandbox_repo: Path) -> dict[str, Any]:
    framework = infer_test_framework(sandbox_repo, str(row.get("Module", "")))
    build_tool = str(row.get("Build_Tool", "Maven"))
    module = str(row.get("Module", ""))
    base_info = {
        "type": build_tool,
        "version": "8.4" if build_tool == "Gradle" else "3.8.1",
        "java_version": row.get("Java_Version", "1.8"),
        **framework,
    }
    info = {str(row["Project"]): dict(base_info)}
    if module:
        info[str(row["Project"])]["modules"] = [module]
        info[f"{row['Project']}_{module}"] = dict(base_info)
    return info


def prepare_sandbox(row: pd.Series, run_dir: Path, compiled_repos: Path) -> Path:
    class_id = f"{int(row['sample_index']):03d}_{safe_name(row['Project'])}_{safe_name(row['Focal_Class'])}_{hashlib.sha1(str(row['class_key']).encode()).hexdigest()[:8]}"
    sandbox = ROOT / "workspaces" / run_dir.name / class_id
    sandbox.mkdir(parents=True, exist_ok=True)
    (sandbox / "compiledrepos").mkdir(parents=True, exist_ok=True)
    source_repo = compiled_repos / str(row["Project"])
    target_repo = sandbox / "compiledrepos" / str(row["Project"])
    copy_repo_to_sandbox(source_repo, target_repo)
    test_rel = normalize_repo_rel_path(row["Synthesized_Test_Rel_Path"])
    test_file = target_repo / test_rel
    test_file.parent.mkdir(parents=True, exist_ok=True)
    test_file.write_text(placeholder_test_source(str(row["Package"]), str(row["Test_Class"])), encoding="utf-8")
    (sandbox / "output" / str(row["Project"])).mkdir(parents=True, exist_ok=True)
    class_row = pd.DataFrame(
        [
            {
                "Project": row["Project"],
                "Focal_Class": row["Focal_Class"],
                "Test_Class": row["Test_Class"],
                "Focal_Path": row["Focal_Path"],
                "Test_Path": row["Test_Path"],
                "Module": row["Module"],
            }
        ]
    )
    class_row.to_csv(sandbox / "output" / "classes.csv", index=False)
    info = project_info_for_row(row, target_repo)
    (sandbox / "output" / "project_info.json").write_text(json.dumps(info, indent=2, ensure_ascii=False), encoding="utf-8")
    for file_name in ["settings.xml", "TestSmellDetector.jar"]:
        src = ROOT / file_name
        if src.exists():
            shutil.copy2(src, sandbox / file_name)
    return sandbox


def parse_agone_metrics(output_csv: Path, row: pd.Series, model: str, prompt_name: str) -> list[dict[str, Any]]:
    arms = [
        ("gpt", model, prompt_name),
        ("evosuite", "evosuite", "-"),
    ]
    out = pd.read_csv(output_csv) if output_csv.exists() and output_csv.stat().st_size else pd.DataFrame()
    result: list[dict[str, Any]] = []
    for arm, generator, prompt in arms:
        found = pd.Series(dtype=object)
        if not out.empty:
            mask = out["Generator(LLM/EVOSUITE)"].astype(str).eq(generator)
            if generator != "evosuite" and "Prompt_Technique" in out.columns:
                mask = mask & out["Prompt_Technique"].astype(str).eq(prompt_name)
            subset = out[mask]
            if not subset.empty:
                found = subset.iloc[-1]
        compilation = int(float(found.get("Compilation", 0))) if not found.empty and str(found.get("Compilation", "")).strip() else 0
        fail_stage = "ok" if compilation == 1 else "missing_or_failed_output"
        values = {
            "branch_coverage": found.get("Branch_Coverage", None) if not found.empty else None,
            "line_coverage": found.get("Line_Coverage", None) if not found.empty else None,
            "method_coverage": found.get("Method_Coverage", None) if not found.empty else None,
            "mutation_coverage": found.get("Mutation_Coverage", None) if not found.empty else None,
        }
        rec = {
            "sample_index": row["sample_index"],
            "class_key": row["class_key"],
            "repo_id": row["Project"],
            "focal_class": row["Focal_Class"],
            "focal_path": row["focal_path"],
            "module": row["Module"],
            "complexity_half": row.get("complexity_half", ""),
            "max_method_cc": row.get("max_method_cc", ""),
            "arm": arm,
            "generator": generator,
            "prompt": prompt,
            "compilation": compilation,
            "fail_stage": fail_stage,
            **values,
        }
        for metric in ["branch_coverage", "line_coverage", "method_coverage", "mutation_coverage"]:
            numeric = pd.to_numeric(pd.Series([rec[metric]]), errors="coerce").iloc[0]
            rec[metric] = numeric if pd.notna(numeric) else None
            rec[f"strict_{metric}"] = float(numeric) if compilation == 1 and pd.notna(numeric) else 0.0
        result.append(rec)
    return result


def copy_generated_tests(sandbox: Path, run_dir: Path, row: pd.Series, model: str, prompt_name: str) -> None:
    out_dir = sandbox / "output" / str(row["Project"])
    dest_dir = run_dir / "generated_tests"
    dest_dir.mkdir(parents=True, exist_ok=True)
    manifest_rows = []
    for path in out_dir.glob("response_*.java"):
        arm = "evosuite" if path.name.startswith("response_evosuite") else "gpt"
        dest = dest_dir / arm / f"{int(row['sample_index']):03d}_{safe_name(row['Project'])}_{safe_name(row['Test_Class'])}.java"
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(path, dest)
        manifest_rows.append(
            {
                "sample_index": row["sample_index"],
                "repo_id": row["Project"],
                "class_key": row["class_key"],
                "arm": arm,
                "source_path": str(path),
                "stored_path": str(dest),
                "size_bytes": dest.stat().st_size,
            }
        )
    for rec in manifest_rows:
        append_csv(run_dir / "generated_tests_manifest.csv", rec)


def run_one_class(row: pd.Series, run_dir: Path, compiled_repos: Path, model: str, prompt_name: str) -> list[dict[str, Any]]:
    started = time.time()
    append_phase(
        run_dir,
        "class_sandbox_start",
        project=row["Project"],
        module=row["Module"],
        sample_index=row["sample_index"],
        class_key=row["class_key"],
        focal_class=row["Focal_Class"],
        test_class=row["Test_Class"],
        status="START",
    )
    try:
        sandbox = prepare_sandbox(row, run_dir, compiled_repos)
    except Exception as exc:
        append_phase(run_dir, "class_sandbox_start", project=row["Project"], status="FAIL", started_at=started, detail=f"{type(exc).__name__}: {exc}")
        return fallback_metrics(row, model, prompt_name, "sandbox_prepare_failed")

    stdout_path = run_dir / "class_logs" / f"{int(row['sample_index']):03d}_{safe_name(row['Project'])}_{safe_name(row['Focal_Class'])}.stdout.log"
    stderr_path = stdout_path.with_suffix(".stderr.log")
    stdout_path.parent.mkdir(parents=True, exist_ok=True)
    env = os.environ.copy()
    env["PYTHONPATH"] = str(ROOT / "AgoneTest")
    env["RBL4_API_LOG_CSV"] = str(run_dir / "api_log.csv")
    env["RBL4_API_PROMPTS_JSONL"] = str(run_dir / "api_prompts.jsonl")
    env["RBL4_PHASE_LOG_CSV"] = str(run_dir / "phase_log.csv")
    env["RBL4_CURRENT_PROJECT"] = str(row["Project"])
    env["RBL4_SAMPLE_INDEX"] = str(row["sample_index"])
    env["RBL4_CLASS_KEY"] = str(row["class_key"])
    command = [
        sys.executable,
        str(ROOT / "agone_one.py"),
        "--sandbox",
        str(sandbox),
        "--project",
        str(row["Project"]),
        "--model",
        model,
        "--prompt",
        prompt_name,
    ]
    with stdout_path.open("w", encoding="utf-8") as stdout, stderr_path.open("w", encoding="utf-8") as stderr:
        result = subprocess.run(
            command,
            cwd=sandbox,
            stdout=stdout,
            stderr=stderr,
            env=env,
            timeout=int(os.getenv("RBL4_CLASS_TIMEOUT_SECONDS", "1800")),
        )
    copy_generated_tests(sandbox, run_dir, row, model, prompt_name)
    agone_output = sandbox / "output" / "output_agone_classes.csv"
    metrics = parse_agone_metrics(agone_output, row, model, prompt_name)
    if result.returncode != 0:
        for rec in metrics:
            if rec["compilation"] != 1:
                rec["fail_stage"] = "agone_runtime_failed"
        detail = f"return_code={result.returncode}; stdout={stdout_path.name}; stderr={stderr_path.name}"
        append_phase(run_dir, "class_agone_run", project=row["Project"], module=row["Module"], status="FAIL", started_at=started, detail=detail)
    else:
        append_phase(run_dir, "class_agone_run", project=row["Project"], module=row["Module"], status="PASS", started_at=started)
    return metrics


def fallback_metrics(row: pd.Series, model: str, prompt_name: str, fail_stage: str) -> list[dict[str, Any]]:
    records = []
    for arm, generator, prompt in [("gpt", model, prompt_name), ("evosuite", "evosuite", "-")]:
        rec = {
            "sample_index": row.get("sample_index", ""),
            "class_key": row.get("class_key", ""),
            "repo_id": row.get("Project", row.get("repo_id", "")),
            "focal_class": row.get("Focal_Class", row.get("focal_class", "")),
            "focal_path": row.get("focal_path", ""),
            "module": row.get("Module", ""),
            "complexity_half": row.get("complexity_half", ""),
            "max_method_cc": row.get("max_method_cc", ""),
            "arm": arm,
            "generator": generator,
            "prompt": prompt,
            "compilation": 0,
            "fail_stage": fail_stage,
            "branch_coverage": None,
            "line_coverage": None,
            "method_coverage": None,
            "mutation_coverage": None,
            "strict_branch_coverage": 0.0,
            "strict_line_coverage": 0.0,
            "strict_method_coverage": 0.0,
            "strict_mutation_coverage": 0.0,
        }
        records.append(rec)
    return records


def build_summary(metrics: pd.DataFrame, source_n: int) -> pd.DataFrame:
    rows = []
    for arm, group in metrics.groupby("arm"):
        rec: dict[str, Any] = {
            "arm": arm,
            "n": int(len(group)),
            "source_n": int(source_n),
            "compilation_success_n": int((group["compilation"] == 1).sum()),
            "compilation_success_rate": round(float((group["compilation"] == 1).mean()), 6) if len(group) else 0.0,
        }
        compiled = group[group["compilation"] == 1]
        for metric in ["branch_coverage", "line_coverage", "method_coverage", "mutation_coverage"]:
            strict = f"strict_{metric}"
            rec[f"{metric}_compiled_mean"] = round(float(compiled[metric].mean()), 6) if not compiled.empty else None
            rec[f"{metric}_compiled_median"] = round(float(compiled[metric].median()), 6) if not compiled.empty else None
            rec[f"{metric}_strict_mean"] = round(float(group[strict].mean()), 6) if len(group) else 0.0
            rec[f"{metric}_strict_median"] = round(float(group[strict].median()), 6) if len(group) else 0.0
        rows.append(rec)
    return pd.DataFrame(rows)


def rq_decisions(metrics: pd.DataFrame) -> pd.DataFrame:
    rows: list[dict[str, Any]] = []
    gpt = metrics[metrics["arm"] == "gpt"].copy()
    evosuite = metrics[metrics["arm"] == "evosuite"].copy()
    compiled = gpt[gpt["compilation"] == 1]
    try:
        from scipy.stats import binomtest, wilcoxon
    except Exception:
        binomtest = None
        wilcoxon = None

    def wilcoxon_greater(values: pd.Series, threshold: float) -> tuple[Any, str]:
        values = values.dropna().astype(float)
        if len(values) < 60:
            return None, "descriptive_only_n_lt_60"
        if wilcoxon is None:
            return None, "scipy_unavailable"
        shifted = values - threshold
        try:
            return float(wilcoxon(shifted, alternative="greater").pvalue), "tested"
        except Exception as exc:
            return None, f"test_error:{type(exc).__name__}"

    p, note = wilcoxon_greater(compiled["mutation_coverage"], REFERENCE["mutation_compiled_only_gpt4o_mini"])
    rows.append(
        {
            "rq": "RQ1",
            "metric": "GPT compiled-only mutation median",
            "n": int(len(compiled)),
            "observed": float(compiled["mutation_coverage"].median()) if len(compiled) else None,
            "threshold": REFERENCE["mutation_compiled_only_gpt4o_mini"],
            "p_value": p,
            "decision_note": note,
        }
    )
    p, note = wilcoxon_greater(compiled["branch_coverage"], REFERENCE["branch_compiled_only_gpt4o_mini"])
    rows.append(
        {
            "rq": "RQ2",
            "metric": "GPT compiled-only branch median",
            "n": int(len(compiled)),
            "observed": float(compiled["branch_coverage"].median()) if len(compiled) else None,
            "threshold": REFERENCE["branch_compiled_only_gpt4o_mini"],
            "p_value": p,
            "decision_note": note,
        }
    )
    success_n = int((gpt["compilation"] == 1).sum())
    p3 = None
    note3 = "scipy_unavailable"
    if binomtest is not None:
        p3 = float(binomtest(success_n, len(gpt), REFERENCE["build_success_gpt4o_mini"], alternative="greater").pvalue)
        note3 = "tested"
    rows.append(
        {
            "rq": "RQ3",
            "metric": "GPT build success rate",
            "n": int(len(gpt)),
            "observed": success_n / len(gpt) if len(gpt) else None,
            "threshold": REFERENCE["build_success_gpt4o_mini"],
            "p_value": p3,
            "decision_note": note3,
        }
    )
    wide = metrics.pivot_table(index="class_key", columns="arm", values="strict_mutation_coverage", aggfunc="last")
    p4 = None
    note4 = "missing_arms"
    observed = None
    if {"gpt", "evosuite"} <= set(wide.columns):
        diff = wide["gpt"] - wide["evosuite"]
        observed = float(diff.median())
        shifted = diff + REFERENCE["rq4_noninferiority_margin_pp"]
        if wilcoxon is not None:
            try:
                p4 = float(wilcoxon(shifted, alternative="greater").pvalue)
                note4 = "tested"
            except Exception as exc:
                note4 = f"test_error:{type(exc).__name__}"
        else:
            note4 = "scipy_unavailable"
    rows.append(
        {
            "rq": "RQ4",
            "metric": "strict mutation median GPT-EvoSuite",
            "n": int(len(wide)),
            "observed": observed,
            "threshold": -REFERENCE["rq4_noninferiority_margin_pp"],
            "p_value": p4,
            "decision_note": note4,
        }
    )
    return pd.DataFrame(rows)


def write_manifest(run_dir: Path, args: argparse.Namespace, status: str, error: str | None, preflight_report: dict[str, Any], metrics: pd.DataFrame | None = None) -> None:
    baseline_pass_n = None
    baseline_failed_n = None
    baseline_classes = run_dir / "baseline_classes.csv"
    if baseline_classes.exists() and baseline_classes.stat().st_size:
        try:
            baseline_df = pd.read_csv(baseline_classes)
            baseline_pass_n = int((baseline_df["baseline_build_status"].astype(str) == "PASS").sum())
            baseline_failed_n = int((baseline_df["baseline_build_status"].astype(str) != "PASS").sum())
        except Exception:
            baseline_pass_n = None
            baseline_failed_n = None
    manifest = {
        "timestamp_utc": utc_now(),
        "run_id": run_dir.name,
        "status": status,
        "error": error,
        "run_mode": args.mode,
        "manifest_csv": str(args.manifest),
        "recipes_csv": str(args.recipes),
        "compiledrepos_root": str(args.compiledrepos),
        "model": args.model,
        "prompt": args.prompt,
        "prompt_hash_sha256": prompt_hash(),
        "prompt_protocol": "AgoneTest base zero-shot from proposal; no java_language_rules.",
        "source_sample_n": int(preflight_report.get("manifest_rows", 0)),
        "buildable_run_n": int(preflight_report.get("manifest_rows", 0)) - int(preflight_report.get("preflight_failed_class_n", 0)),
        "precheck_skipped_n": int(preflight_report.get("preflight_failed_class_n", 0)),
        "baseline_pass_n": baseline_pass_n,
        "baseline_failed_n": baseline_failed_n,
        "repo_n": int(preflight_report.get("manifest_repos", 0)),
        "build_scope_n": int(preflight_report.get("recipe_scopes", 0)),
        "gpt_rows": int((metrics["arm"] == "gpt").sum()) if metrics is not None and not metrics.empty else 0,
        "evosuite_rows": int((metrics["arm"] == "evosuite").sum()) if metrics is not None and not metrics.empty else 0,
        "fairness_policy": {
            "replacement_after_dataset_lock": False,
            "generated_test_repair": False,
            "prompt": "original AgoneTest zero-shot prompt from proposal",
            "sandbox_policy": "one focal class per sandbox; only that generated test is installed and measured",
            "baseline_gate": "all 300 classes/scopes must pass baseline before generation starts",
            "failure_scoring": "failed generated tests are compilation=0 and strict metrics=0",
        },
    }
    (run_dir / "manifest.json").write_text(json.dumps(manifest, indent=2, ensure_ascii=False), encoding="utf-8")


def write_status(run_dir: Path, status: str, args: argparse.Namespace, error: str | None = None) -> None:
    existing = {}
    status_path = run_dir / "status.json"
    if status_path.exists():
        try:
            existing = json.loads(status_path.read_text(encoding="utf-8"))
        except Exception:
            existing = {}
    doc = {
        "run_id": run_dir.name,
        "status": status,
        "run_mode": args.mode,
        "sample_csv": str(args.manifest),
        "model": args.model,
        "prompt": args.prompt,
        "created_at": existing.get("created_at", utc_now()),
        "started_at": existing.get("started_at", utc_now()),
        "pid": existing.get("pid", os.getpid()),
        "return_code": existing.get("return_code"),
        "updated_at": utc_now(),
        "error": error,
    }
    status_path.write_text(json.dumps(doc, indent=2, ensure_ascii=False), encoding="utf-8")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="RBL-4 v2 official runner for data_new.")
    parser.add_argument("--manifest", type=Path, default=DEFAULT_MANIFEST)
    parser.add_argument("--recipes", type=Path, default=DEFAULT_RECIPES)
    parser.add_argument("--compiledrepos", type=Path, default=DEFAULT_COMPILED_REPOS)
    parser.add_argument("--results-dir", type=Path, default=DEFAULT_RESULTS_DIR)
    parser.add_argument("--run-id", default="")
    parser.add_argument("--mode", choices=["dry_run", "baseline_only", "full_run"], default="dry_run")
    parser.add_argument("--model", default=DEFAULT_MODEL)
    parser.add_argument("--prompt", default=DEFAULT_PROMPT)
    parser.add_argument("--limit", type=int, default=0, help="Debug only: process first N classes after preflight/baseline.")
    parser.add_argument("--keep-workspaces", action="store_true")
    return parser.parse_args()


def main() -> int:
    os.chdir(ROOT)
    load_local_env()
    args = parse_args()
    args.manifest = args.manifest.resolve()
    args.recipes = args.recipes.resolve()
    args.compiledrepos = args.compiledrepos.absolute()
    args.results_dir.mkdir(parents=True, exist_ok=True)
    run_id = args.run_id or f"{datetime.now().strftime('%Y%m%d_%H%M%S')}_{uuid.uuid4().hex[:8]}"
    run_dir = args.results_dir / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    write_status(run_dir, "running", args)
    preflight_report: dict[str, Any] = {}
    metrics_df: pd.DataFrame | None = None
    try:
        write_run_settings(args.model, args.prompt)
        dataset = load_dataset(args.manifest, args.recipes, args.compiledrepos)
        sample, preflight_report = preflight(dataset, args.compiledrepos, run_dir)
        sample.to_csv(run_dir / "staged_classes.csv", index=False)
        if preflight_report["manifest_rows"] != 300 or preflight_report["manifest_repos"] != 30:
            raise RuntimeError("Dataset shape is not 300 classes / 30 repos.")
        hard_preflight_fail = sample[sample["preflight_status"] != "PASS"]
        if args.mode == "dry_run":
            status = "completed" if hard_preflight_fail.empty else "failed"
            error = None if hard_preflight_fail.empty else f"Preflight failed for {len(hard_preflight_fail)} classes."
            write_manifest(run_dir, args, status, error, preflight_report)
            write_status(run_dir, status, args, error)
            return 0 if hard_preflight_fail.empty else 2
        if not hard_preflight_fail.empty:
            raise RuntimeError(f"Preflight failed for {len(hard_preflight_fail)} classes; see preflight_classes.csv.")

        baseline_scope_df, baseline_class_df = run_baseline_scopes(sample, run_dir)
        baseline_failed = baseline_class_df[baseline_class_df["baseline_build_status"] != "PASS"]
        if args.mode == "baseline_only":
            status = "completed" if baseline_failed.empty else "failed"
            error = None if baseline_failed.empty else f"Baseline failed for {len(baseline_failed)} classes."
            write_manifest(run_dir, args, status, error, preflight_report)
            write_status(run_dir, status, args, error)
            return 0 if baseline_failed.empty else 3
        if not baseline_failed.empty:
            raise RuntimeError(f"Baseline gate failed for {len(baseline_failed)} classes; generation not started.")

        generation_sample = baseline_class_df.copy()
        if args.limit > 0:
            generation_sample = generation_sample.head(args.limit).copy()
        all_metrics: list[dict[str, Any]] = []
        for _, row in generation_sample.iterrows():
            all_metrics.extend(run_one_class(row, run_dir, args.compiledrepos, args.model, args.prompt))
            metrics_df = pd.DataFrame(all_metrics)
            metrics_df.to_csv(run_dir / "metrics_long.csv", index=False)
            build_summary(metrics_df, source_n=len(sample)).to_csv(run_dir / "summary.csv", index=False)
            rq_decisions(metrics_df).to_csv(run_dir / "rq_decisions.csv", index=False)
        metrics_df = pd.DataFrame(all_metrics)
        metrics_df.to_csv(run_dir / "metrics_long.csv", index=False)
        failures = metrics_df[(metrics_df["compilation"] != 1) | (metrics_df["fail_stage"] != "ok")].copy()
        failures.to_csv(run_dir / "generated_failures.csv", index=False)
        build_summary(metrics_df, source_n=len(sample)).to_csv(run_dir / "summary.csv", index=False)
        rq_decisions(metrics_df).to_csv(run_dir / "rq_decisions.csv", index=False)
        write_manifest(run_dir, args, "completed", None, preflight_report, metrics_df)
        write_status(run_dir, "completed", args)
        if not args.keep_workspaces:
            # Keep workspaces by default during development only when requested; deletion is intentionally
            # not performed here to preserve reproducibility artifacts in this environment.
            pass
        return 0
    except Exception as exc:
        error = f"{type(exc).__name__}: {exc}"
        append_phase(run_dir, "runner", status="ERROR", detail=error)
        write_manifest(run_dir, args, "failed", error, preflight_report, metrics_df)
        write_status(run_dir, "failed", args, error)
        print(error, file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
