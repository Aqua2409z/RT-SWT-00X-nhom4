from __future__ import annotations

import argparse
import json
import math
from pathlib import Path
from typing import Any

import pandas as pd


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_MANIFEST = ROOT / "class_sampling_manifest_seed42.csv"
DEFAULT_OUTPUT_DIR = ROOT / "output"
DEFAULT_REPORT = ROOT / "metadata" / "pilot_60_selection_report.json"
SORT_COLUMNS = ["repo_order_index", "selection_rank", "max_method_cc", "focal_class", "focal_path"]
RECIPE_COLUMNS = [
    "java_version",
    "java_version_source",
    "build_tool",
    "dependency_command",
    "compile_command",
    "test_compile_command",
    "working_directory",
    "skip_flags",
    "disabled_components",
    "build_recipe_id",
    "validation_log",
    "success_origin",
]


def read_build_recipes(path: Path) -> dict[str, dict[str, Any]]:
    if not path.exists():
        return {}
    recipes: dict[str, dict[str, Any]] = {}
    for raw in path.read_text(encoding="utf-8").splitlines():
        if not raw.strip():
            continue
        row = json.loads(raw)
        repo_id = str(row.get("repo_id", "")).strip()
        if repo_id:
            recipes[repo_id] = row
    return recipes


def enrich_with_recipes(df: pd.DataFrame, recipes: dict[str, dict[str, Any]]) -> pd.DataFrame:
    result = df.copy()
    repo_ids = result["repo_id"].astype(str)
    for column in RECIPE_COLUMNS:
        if column in result.columns:
            continue
        result[column] = [recipes.get(repo_id, {}).get(column, "") for repo_id in repo_ids]
    return result


def split_main_backup(df: pd.DataFrame) -> tuple[pd.DataFrame, pd.DataFrame]:
    if "selected_type" not in df.columns:
        raise ValueError("Manifest must contain selected_type column")
    main = df[df["selected_type"].astype(str).str.lower().eq("main")].copy()
    backup = df[df["selected_type"].astype(str).str.lower().eq("backup")].copy()
    return main.sort_values(SORT_COLUMNS, kind="mergesort"), backup.sort_values(SORT_COLUMNS, kind="mergesort")


def hamilton_repo_allocation(unique_main: pd.DataFrame, target_n: int) -> dict[str, int]:
    repo_order = unique_main.groupby("repo_id")["repo_order_index"].min().to_dict()
    counts = unique_main["repo_id"].value_counts().to_dict()
    repo_ids = sorted(counts, key=lambda repo: (repo_order[repo], repo))
    if target_n < len(repo_ids):
        raise ValueError(f"target_n={target_n} is smaller than repo count {len(repo_ids)}")

    allocation = {repo: 1 for repo in repo_ids}
    remaining_slots = target_n - len(repo_ids)
    weights = {repo: counts[repo] - 1 for repo in repo_ids}
    weight_total = sum(weights.values())
    if weight_total <= 0:
        return allocation

    quotas = {repo: weights[repo] / weight_total * remaining_slots for repo in repo_ids}
    floors = {repo: math.floor(quotas[repo]) for repo in repo_ids}
    for repo, value in floors.items():
        allocation[repo] += value

    extra = remaining_slots - sum(floors.values())
    remainder_order = sorted(
        repo_ids,
        key=lambda repo: (quotas[repo] - floors[repo], counts[repo], -repo_order[repo]),
        reverse=True,
    )
    for repo in remainder_order[:extra]:
        allocation[repo] += 1
    return allocation


def select_unique_pilot(main: pd.DataFrame, target_n: int) -> pd.DataFrame:
    unique_main = (
        main.sort_values(SORT_COLUMNS + ["test_path"], kind="mergesort")
        .drop_duplicates(["repo_id", "focal_path", "test_path"], keep="first")
        .sort_values(SORT_COLUMNS, kind="mergesort")
    )
    allocation = hamilton_repo_allocation(unique_main, target_n)
    selected_indices: list[int] = []
    repo_order = unique_main.groupby("repo_id")["repo_order_index"].min().to_dict()
    repo_ids = sorted(allocation, key=lambda repo: (repo_order[repo], repo))
    for repo_id in repo_ids:
        group = unique_main[unique_main["repo_id"].eq(repo_id)]
        selected_indices.extend(group.head(allocation[repo_id]).index.tolist())

    pilot = unique_main.loc[selected_indices].sort_values(SORT_COLUMNS, kind="mergesort").copy()
    pilot["pilot_seed"] = 42
    pilot["pilot_60_rank"] = range(1, len(pilot) + 1)
    pilot["pilot_selection_policy"] = "unique_focal_per_repo_hamilton_repo_weighted_seed42"
    return pilot


def write_main_parts(main: pd.DataFrame, output_dir: Path) -> list[Path]:
    files: list[Path] = []
    chunk_size = math.ceil(len(main) / 3)
    for index in range(3):
        part = main.iloc[index * chunk_size : (index + 1) * chunk_size].copy()
        path = output_dir / f"classes_part{index + 1}.csv"
        part.to_csv(path, index=False)
        files.append(path)
    return files


def value_counts_dict(series: pd.Series) -> dict[str, int]:
    return {str(key): int(value) for key, value in series.value_counts().sort_index().items()}


def build_report(
    manifest_path: Path,
    manifest: pd.DataFrame,
    main: pd.DataFrame,
    backup: pd.DataFrame,
    pilot: pd.DataFrame,
    files: list[Path],
) -> dict[str, Any]:
    return {
        "source_manifest": str(manifest_path.relative_to(ROOT) if manifest_path.is_relative_to(ROOT) else manifest_path),
        "seed": 42,
        "selection_policy": "Pilot 60 uses unique repo_id+focal_path+test_path rows: one per repo first, then Hamilton largest-remainder allocation over remaining unique main classes.",
        "manifest_rows": int(len(manifest)),
        "main_rows": int(len(main)),
        "main_unique_focal_test_rows": int(main.drop_duplicates(["repo_id", "focal_path", "test_path"]).shape[0]),
        "backup_rows": int(len(backup)),
        "backup_unique_focal_test_rows": int(backup.drop_duplicates(["repo_id", "focal_path", "test_path"]).shape[0]),
        "represented_repositories": int(manifest["repo_id"].nunique()),
        "pilot_rows": int(len(pilot)),
        "pilot_repositories": int(pilot["repo_id"].nunique()),
        "pilot_duplicate_focal_test_rows": int(pilot.duplicated(["repo_id", "focal_path", "test_path"]).sum()),
        "main_cc_distribution": value_counts_dict(main["max_method_cc"]),
        "pilot_cc_distribution": value_counts_dict(pilot["max_method_cc"]),
        "pilot_complexity_bucket_distribution": value_counts_dict(pilot["complexity_bucket"]),
        "pilot_repo_distribution": value_counts_dict(pilot["repo_id"]),
        "files_written": [str(path.relative_to(ROOT)) for path in files],
    }


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Create RBL-4 pilot/full sample CSVs from the seed-42 manifest.")
    parser.add_argument("--manifest", type=Path, default=DEFAULT_MANIFEST)
    parser.add_argument("--output-dir", type=Path, default=DEFAULT_OUTPUT_DIR)
    parser.add_argument("--report", type=Path, default=DEFAULT_REPORT)
    parser.add_argument("--pilot-size", type=int, default=60)
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    manifest_path = args.manifest.resolve()
    output_dir = args.output_dir.resolve()
    report_path = args.report.resolve()

    recipes = read_build_recipes(ROOT / "metadata" / "build_recipes.jsonl")
    manifest = pd.read_csv(manifest_path, dtype={"repo_id": str})
    manifest = enrich_with_recipes(manifest, recipes)
    main_df, backup_df = split_main_backup(manifest)
    pilot_df = select_unique_pilot(main_df, args.pilot_size)

    output_dir.mkdir(parents=True, exist_ok=True)
    report_path.parent.mkdir(parents=True, exist_ok=True)

    files = [
        output_dir / "classes_main.csv",
        output_dir / "classes_backup.csv",
        output_dir / "pilot_60_classes.csv",
        output_dir / "pilot_60_backups.csv",
    ]
    main_df.to_csv(files[0], index=False)
    backup_df.to_csv(files[1], index=False)
    pilot_df.to_csv(files[2], index=False)
    backup_df.drop_duplicates(["repo_id", "focal_path", "test_path"], keep="first").to_csv(files[3], index=False)
    files.extend(write_main_parts(main_df, output_dir))

    report = build_report(manifest_path, manifest, main_df, backup_df, pilot_df, files)
    report_path.write_text(json.dumps(report, indent=2, ensure_ascii=False), encoding="utf-8")

    print(f"Wrote {len(pilot_df)} pilot rows covering {pilot_df['repo_id'].nunique()} repos")
    print(f"Wrote full main rows: {len(main_df)}")
    print(f"Wrote report: {report_path.relative_to(ROOT)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
