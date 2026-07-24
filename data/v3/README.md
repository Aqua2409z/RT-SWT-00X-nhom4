# Data V3 — canonical pre-experiment dataset

This directory contains the Data V3 artifacts validated by pipeline version
`3.1.0-pre-experiment` before any GPT, EvoSuite, JaCoCo, or PIT execution.
Do not edit the sealed CSV or JSON artifacts directly.

## Canonical files

| Purpose | Canonical file |
|---|---|
| 30 repositories and exact commits | `successful_repos_manifest.csv` |
| 300 main focal classes | `class_sampling_manifest_final_seed42.csv` |
| 60 backup focal classes | `class_backup_manifest_seed42.csv` |
| Per-repository 10+2 counts | `repo_sampling_summary.csv` |
| Cross-platform build commands | `build_recipes_portable.csv` |
| Complete build-attempt history | `build_attempts.csv` |
| Build-attempt and log checksums | `BUILD_EVIDENCE_SHA256SUMS.csv` |
| Final invariant report | `results/validation_report.md` |
| Scientific-integrity safeguards | `results/data_integrity_report.md` |
| Sampling algorithm | `results/sampling_methodology.md` |
| Relative CC-half statistics | `results/complexity_halves_summary.json` |
| Recorded environment | `results/environment_versions.json` |
| Frozen readiness state | `results/RUN_READY` |
| Principal source/output checksums | `results/SHA256SUMS.csv` |

`class_sampling_manifest_seed42.csv` is the sample before the post-selection
complexity-half labels were added. Experiments must use
`class_sampling_manifest_final_seed42.csv`.

## Sampling protocol

The sampling unit is one physical focal Java source file identified by
`repo_id` and normalized focal path. Repositories are screened in a
deterministic seed-42/SHA-256 order. The first 30 qualified repositories each
contribute exactly 10 main classes and 2 backup classes.

Cyclomatic Complexity is not an eligibility or selection variable. After all
300 main classes are selected, the pipeline ranks them by `max_method_cc` and
then `sum_method_cc` to form two relative halves of 150 classes each. Therefore,
`lower_complexity_half` and `higher_complexity_half` are relative labels rather
than absolute CC thresholds.

A backup may be used only for a documented pre-outcome technical failure.
Every replacement must record `replacement_of` and `replacement_reason`.
Unfavorable GPT or EvoSuite outcomes are never valid replacement reasons.

## Build evidence

`build_attempts.csv` is immutable historical evidence. Its `command` and
`working_directory` columns may contain absolute creator-machine paths and must
not be copied as replay commands.

Use `build_recipes_portable.csv` for replay. The runner replaces
`${REPO_DIR}` with a writable workspace path and selects the Windows or POSIX
command column as appropriate.

## Content intentionally excluded from Git

Git does not contain:

- `repos/successful/` with the 30 frozen repository checkouts;
- the 1,567 validation and build logs;
- `raw_mapping_index.csv` or `unique_focal_class_frame.csv`;
- `V3_BUILD_BUNDLE.tar.gz`, the delivery ZIP, or the Docker image TAR;
- Maven/Gradle caches or build outputs.

These files belong to the large external delivery. Its metadata and checksums
are under [`external_artifacts/`](external_artifacts/). Always run
`verify_delivery.py` before extracting or using a received archive.

For full-bundle verification and build replay, see
[`../../scripts/build_handoff_v3/README.md`](../../scripts/build_handoff_v3/README.md).
