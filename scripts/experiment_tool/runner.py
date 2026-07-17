from __future__ import annotations

import argparse
import contextlib
import csv
import importlib
import json
import os
import shutil
import sys
import time
import traceback
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import pandas as pd

from .events import append_event
from .reports import build_error_summary, collect_generated_tests, write_excel_report
from .schemas import DEFAULT_MODEL, DEFAULT_PROMPT


BASE_DIR = Path(__file__).resolve().parents[1]
RUNS_DIR = BASE_DIR / "results" / "runs"
AGONE_OUTPUTS = [
    Path("output/output_agone_classes.csv"),
    Path("output/output_agone_projects.csv"),
    Path("output/output_agone_mean.csv"),
    Path("output/output_agone_mean_filtered.csv"),
    Path("output/output_agone_classes_filtered.csv"),
    Path("output/output_agone_classes_filtered_compilation.csv"),
    Path("output/output_agone_info.txt"),
]
REFERENCE = {
    "mutation_compiled_only_gpt4o_mini": 44.5,
    "branch_compiled_only_gpt4o_mini": 41.9,
    "build_success_gpt4o_mini": 0.286,
    "rq4_noninferiority_margin_pp": 5.0,
}


def utc_now() -> str:
    return datetime.now(timezone.utc).isoformat(timespec="seconds")


def load_legacy_runner():
    os.chdir(BASE_DIR)
    sys.path.insert(0, str(BASE_DIR))
    module = importlib.import_module("run_rbl4_part1_buildable_experiment")
    module.DEFAULT_SAMPLE = Path("output/classes_main.csv")
    return module


def backup_file(path: Path, backup_dir: Path) -> Path | None:
    if not path.exists():
        return None
    backup_path = backup_dir / path.as_posix().replace("/", "__")
    backup_path.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(path, backup_path)
    return backup_path


def restore_file(path: Path, backup_path: Path | None) -> None:
    if backup_path is None:
        with contextlib.suppress(FileNotFoundError):
            path.unlink()
        return
    path.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(backup_path, path)


def compiled_paths(row: pd.Series) -> tuple[Path, Path]:
    focal = Path(str(row["Focal_Path"]).replace("repos/", "compiledrepos/"))
    test = Path(str(row["Test_Path"]).replace("repos/", "compiledrepos/"))
    return focal, test


def detect_module(row: pd.Series) -> str:
    focal, test = compiled_paths(row)
    project = str(row["Project"])
    project_root = Path("compiledrepos") / project
    search_from = test.parent if test.exists() else focal.parent
    current = search_from
    while current != current.parent:
        if (current / "pom.xml").exists() or (current / "build.gradle").exists() or (current / "build.gradle.kts").exists():
            try:
                rel = current.relative_to(project_root).as_posix()
            except ValueError:
                return ""
            return "" if rel == "." else rel
        if current == project_root:
            break
        current = current.parent
    return ""


def precheck_sample(sample_csv: Path) -> tuple[pd.DataFrame, pd.DataFrame]:
    df = pd.read_csv(sample_csv)
    expected = {"Project", "Focal_Class", "Test_Class", "Focal_Path", "Test_Path"}
    missing_cols = expected - set(df.columns)
    if missing_cols:
        raise ValueError(f"{sample_csv} missing columns: {sorted(missing_cols)}")

    kept_rows: list[dict[str, Any]] = []
    skipped_rows: list[dict[str, Any]] = []
    for index, row in df.iterrows():
        project = str(row["Project"])
        focal, test = compiled_paths(row)
        reasons = []
        if not (Path("compiledrepos") / project).exists():
            reasons.append("compiledrepo_missing")
        if not focal.exists():
            reasons.append("focal_file_missing")
        if not test.exists():
            reasons.append("test_file_missing")

        record = row.to_dict()
        record["source_row_index"] = index
        record["compiled_focal_path"] = focal.as_posix()
        record["compiled_test_path"] = test.as_posix()
        if reasons:
            record["skip_reason"] = ";".join(reasons)
            skipped_rows.append(record)
            continue
        record["Module"] = detect_module(row)
        kept_rows.append(record)

    kept = pd.DataFrame(kept_rows)
    skipped = pd.DataFrame(skipped_rows)
    if kept.empty:
        raise RuntimeError("No buildable rows remain after precheck.")
    return kept, skipped


def clear_agone_outputs() -> None:
    for path in AGONE_OUTPUTS:
        with contextlib.suppress(FileNotFoundError):
            path.unlink()


def write_manifest(run_dir: Path, payload: dict[str, Any]) -> None:
    (run_dir / "manifest.json").write_text(json.dumps(payload, indent=2, ensure_ascii=False), encoding="utf-8")


def write_csv(path: Path, rows: list[dict[str, Any]], fieldnames: list[str]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


def run_experiment(args: argparse.Namespace) -> int:
    os.chdir(BASE_DIR)
    run_dir = RUNS_DIR / args.run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    run_started_epoch = time.time()
    phase_log = run_dir / "phase_log.csv"
    os.environ["RBL4_PHASE_LOG_CSV"] = str(phase_log.resolve())
    os.environ["RBL4_RUN_ID"] = args.run_id

    append_event(phase_log, "run", "START", "Experiment runner started")
    legacy = load_legacy_runner()
    legacy.load_local_env()
    legacy.ensure_compiledrepos_available()

    sample_csv = Path(args.sample_csv)
    if not sample_csv.is_absolute():
        sample_csv = BASE_DIR / sample_csv
    sample_csv = sample_csv.resolve()
    source_sample_n = int(len(pd.read_csv(sample_csv)))

    backups_dir = run_dir / "backups"
    staged_paths = [
        Path("output/classes.csv"),
        Path("output/project_info.json"),
        Path("AgoneTest/run_settings.yaml"),
        *AGONE_OUTPUTS,
    ]
    backups = {path: backup_file(path, backups_dir) for path in staged_paths}

    sample_df = pd.DataFrame()
    skipped_df = pd.DataFrame()
    baseline_df = pd.DataFrame()
    generation_df = pd.DataFrame()
    run_mode = args.mode
    try:
        append_event(phase_log, "precheck", "START", f"Loading {sample_csv.name}")
        sample_df, skipped_df = legacy.precheck_sample(sample_csv)
        sample_df.to_csv(run_dir / "staged_classes.csv", index=False)
        skipped_df.to_csv(run_dir / "skipped_classes.csv", index=False)
        append_event(
            phase_log,
            "precheck",
            "PASS",
            f"Buildable rows: {len(sample_df)}; skipped rows: {len(skipped_df)}",
        )

        project_info = legacy.project_info_for_buildable_sample(sample_df)
        checks = legacy.check_environment(sample_df, args.model)
        write_csv(run_dir / "environment_checks.csv", checks, ["check", "status", "detail"])
        hard_fail = [check for check in checks if check["status"] == "FAIL"]
        if hard_fail and run_mode == "full_run":
            raise RuntimeError(f"Environment checks failed: {hard_fail}")

        if run_mode == "full_run":
            append_event(phase_log, "baseline_build", "START", "Checking clean build before GPT/EvoSuite generation")
            baseline_df = legacy.run_baseline_builds(sample_df, project_info, phase_log)
            baseline_df.to_csv(run_dir / "baseline_build.csv", index=False)
            generation_df = legacy.filter_baseline_passed_sample(sample_df, baseline_df)
            append_event(
                phase_log,
                "baseline_build",
                "PASS" if len(generation_df) > 0 else "FAIL",
                f"Baseline pass rows: {len(generation_df)}; baseline failed rows: {len(sample_df) - len(generation_df)}",
            )
        else:
            baseline_df = pd.DataFrame(columns=legacy.BASELINE_COLUMNS)
            baseline_df.to_csv(run_dir / "baseline_build.csv", index=False)
            generation_df = sample_df.copy()

        generation_df.to_csv(run_dir / "generation_classes.csv", index=False)
        project_info_for_generation = legacy.project_info_for_buildable_sample(generation_df) if not generation_df.empty else {}
        append_event(phase_log, "stage", "START", "Writing AgoneTest staging files")
        legacy.write_stage_files(generation_df, project_info_for_generation)
        legacy.write_run_settings(args.model, args.prompt)
        (run_dir / "project_info.json").write_text(json.dumps(project_info_for_generation, indent=2, ensure_ascii=False), encoding="utf-8")
        append_event(phase_log, "stage", "PASS", f"Staging files ready; generation rows: {len(generation_df)}")

        if args.clear_agone_output and run_mode == "full_run":
            clear_agone_outputs()

        if run_mode == "full_run" and not generation_df.empty:
            append_event(phase_log, "generation", "START", "Running EvoSuite and GPT arms")
            api_log_path = run_dir / "api_log.csv"
            runtime_errors_path = run_dir / "runtime_errors.csv"
            legacy.patch_litellm_logging(api_log_path)
            projects = sorted(generation_df["Project"].astype(str).unique().tolist())
            legacy.run_agone(
                args.model,
                args.prompt,
                execution_override=not args.resume,
                projects=projects,
                runtime_errors_csv=runtime_errors_path,
                phase_log_csv=phase_log,
            )
            append_event(phase_log, "generation", "PASS", "AgoneTest run finished")
        elif run_mode == "full_run":
            append_event(phase_log, "generation", "SKIP", "No classes passed baseline build")
        elif run_mode == "report_only":
            append_event(phase_log, "generation", "SKIP", "Report-only mode uses existing AgoneTest outputs")
        else:
            append_event(phase_log, "generation", "SKIP", "Dry-run mode does not call OpenAI, EvoSuite, JaCoCo, or PIT")

        append_event(phase_log, "report", "START", "Building CSV and XLSX reports")
        legacy.DEFAULT_SAMPLE = sample_csv
        metrics_df = legacy.build_metrics_long(sample_df, args.model, args.prompt, run_mode, baseline_df=baseline_df if run_mode == "full_run" else None)
        metrics_df["source_sample"] = str(sample_csv)
        summary_df = legacy.build_summary(metrics_df, source_n=source_sample_n, skipped_n=len(skipped_df))
        if "run_scope" in summary_df.columns:
            summary_df["run_scope"] = "rbl4_experiment_tool"
        metrics_df.to_csv(run_dir / "metrics_long.csv", index=False)
        failures_df = metrics_df[(metrics_df["compilation"] != 1) | (metrics_df["fail_stage"] != "ok")].copy()
        failures_df.to_csv(run_dir / "generated_failures.csv", index=False)
        summary_df.to_csv(run_dir / "summary.csv", index=False)
        if run_mode != "dry_run":
            artifact_df = generation_df if not generation_df.empty else (sample_df if run_mode == "report_only" else pd.DataFrame())
            collect_generated_tests(
                run_dir,
                BASE_DIR / "output",
                sorted(artifact_df["Project"].astype(str).unique().tolist()) if not artifact_df.empty else [],
                modified_after_epoch=run_started_epoch if run_mode == "full_run" else None,
            )
        else:
            pd.DataFrame(
                columns=["project", "arm", "file_name", "source_path", "stored_path", "size_bytes", "modified_at"]
            ).to_csv(run_dir / "generated_tests_manifest.csv", index=False)
        build_error_summary(run_dir)

        manifest = {
            "timestamp_utc": utc_now(),
            "status": "completed",
            "error": None,
            "run_id": args.run_id,
            "run_mode": run_mode,
            "source_sample_csv": str(sample_csv),
            "source_sample_n": source_sample_n,
            "buildable_run_n": int(len(sample_df)),
            "precheck_skipped_n": int(len(skipped_df)),
            "baseline_pass_n": int((baseline_df["status"].astype(str).str.upper() == "PASS").sum()) if not baseline_df.empty else None,
            "baseline_failed_n": int((baseline_df["status"].astype(str).str.upper() != "PASS").sum()) if not baseline_df.empty else None,
            "generation_run_n": int(len(generation_df)),
            "model": args.model,
            "prompt": args.prompt,
            "arms": ["evosuite", args.model],
            "references": REFERENCE,
            "projects_run": sorted(generation_df["Project"].astype(str).unique().tolist()) if not generation_df.empty else [],
            "projects_buildable": sorted(sample_df["Project"].astype(str).unique().tolist()),
            "projects_baseline_failed": sorted(
                baseline_df[baseline_df["status"].astype(str).str.upper() != "PASS"]["project"].astype(str).unique().tolist()
            ) if not baseline_df.empty else [],
            "projects_skipped": sorted(skipped_df["Project"].astype(str).unique().tolist()) if not skipped_df.empty else [],
            "fairness_policy": {
                "generated_test_repair": False,
                "source_code_repair": False,
                "formatter_or_license_rewrite": False,
                "agone_correct_flag": False,
                "failure_handling": "baseline build failures are recorded as repo_baseline_failed; generated tests that do not build are recorded with compilation=0 and zero-filled strict metrics",
                "secrets_exported": False,
            },
        }
        write_manifest(run_dir, manifest)
        write_excel_report(run_dir)
        append_event(phase_log, "report", "PASS", "Reports written")
        append_event(phase_log, "run", "PASS", "Experiment completed")
        return 0
    except BaseException as exc:
        if isinstance(exc, KeyboardInterrupt):
            status = "cancelled"
            append_event(phase_log, "run", "CANCELLED", "Experiment cancelled")
        else:
            status = "failed"
            append_event(phase_log, "run", "ERROR", f"{type(exc).__name__}: {exc}")
        (run_dir / "error.log").write_text(traceback.format_exc(), encoding="utf-8")
        write_manifest(
            run_dir,
            {
                "timestamp_utc": utc_now(),
                "status": status,
                "error": f"{type(exc).__name__}: {exc}",
                "run_id": args.run_id,
                "run_mode": run_mode,
                "source_sample_csv": str(sample_csv) if "sample_csv" in locals() else args.sample_csv,
                "source_sample_n": source_sample_n if "source_sample_n" in locals() else None,
                "buildable_run_n": int(len(sample_df)) if not sample_df.empty else 0,
                "precheck_skipped_n": int(len(skipped_df)) if not skipped_df.empty else 0,
                "baseline_pass_n": int((baseline_df["status"].astype(str).str.upper() == "PASS").sum()) if not baseline_df.empty else None,
                "baseline_failed_n": int((baseline_df["status"].astype(str).str.upper() != "PASS").sum()) if not baseline_df.empty else None,
                "generation_run_n": int(len(generation_df)) if not generation_df.empty else None,
                "model": args.model,
                "prompt": args.prompt,
                "references": REFERENCE,
            },
        )
        with contextlib.suppress(Exception):
            if run_mode != "dry_run" and not sample_df.empty:
                artifact_df = generation_df if not generation_df.empty else (sample_df if run_mode == "report_only" else pd.DataFrame())
                collect_generated_tests(
                    run_dir,
                    BASE_DIR / "output",
                    sorted(artifact_df["Project"].astype(str).unique().tolist()) if not artifact_df.empty else [],
                    modified_after_epoch=run_started_epoch if run_mode == "full_run" else None,
                )
            build_error_summary(run_dir)
            write_excel_report(run_dir)
        return 130 if isinstance(exc, KeyboardInterrupt) else 1
    finally:
        if not args.keep_staged_files:
            for path, backup_path in backups.items():
                with contextlib.suppress(Exception):
                    restore_file(path, backup_path)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Run the RBL-4 experiment tool worker.")
    parser.add_argument("--run-id", required=True)
    parser.add_argument("--sample-csv", required=True)
    parser.add_argument("--mode", choices=["dry_run", "report_only", "full_run"], default="dry_run")
    parser.add_argument("--model", default=DEFAULT_MODEL)
    parser.add_argument("--prompt", default=DEFAULT_PROMPT)
    parser.add_argument("--resume", action="store_true")
    parser.add_argument("--clear-agone-output", action="store_true")
    parser.add_argument("--keep-staged-files", action="store_true")
    return parser.parse_args()


def main() -> int:
    return run_experiment(parse_args())


if __name__ == "__main__":
    raise SystemExit(main())
