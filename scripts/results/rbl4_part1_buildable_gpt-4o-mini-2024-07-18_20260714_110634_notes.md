# RBL-4 Part 1 Buildable-Only Experiment Note

- Timestamp UTC: `2026-07-14T04:06:40+00:00`
- Run mode: `dry_run`
- Source sample: `output\pilot_24_classes.csv`
- Source N: `24`
- Buildable N run: `23`
- Precheck skipped N: `1`
- GPT model: `gpt-4o-mini-2024-07-18`
- Prompt: `rbl4-zero-shot`
- LLM configuration: temperature `0`, top_p `1`, max output tokens `2048`.
- EvoSuite baseline: `evosuite-1.2.0.jar` via AgoneTest.
- Measurement tools: existing AgoneTest JaCoCo/PIT/TestSmellDetector integration.
- Fairness policy: no generated-test repair, no source-code repair, no formatter/license rewrite to make a test pass.
- Build/coverage instrumentation may temporarily edit build files for JaCoCo/PIT/EvoSuite wiring, then restore them; these edits are not used to fix generated tests.

## Protocol Deviation

This run intentionally skips part-1 classes whose repositories are not buildable or whose source/test files are absent in `compiledrepos/`. Skipped rows are logged, not replaced.
Because this is Part 1 and buildable-only, RQ1/RQ2 are descriptive when GPT compiled-success rows are below 60, following the proposal safeguard.
When run mode is `dry_run`, metrics are placeholders for pipeline validation and must not be reported as experimental outcomes.

## Skipped Classes

- `88231534_TracingCommandListener` skipped: `test_file_missing`

## Summary

| run_scope                 | run_mode | source_sample_n | buildable_run_n | precheck_skipped_n | arm      | compiled_success_n | compiled_success_rate | branch_compiled_mean | branch_compiled_median | line_compiled_mean | line_compiled_median | method_compiled_mean | method_compiled_median | mutation_compiled_mean | mutation_compiled_median | branch_strict_zero_fill_mean | mutation_strict_zero_fill_mean | rq3_binomial_p_greater_0_286 | rq1_rq2_interpretation            | rq4_wilcoxon_p_noninferiority_margin_5pp | rq4_median_gpt_minus_evosuite_pp | rq4_vargha_delaney_a12_gpt_vs_evosuite |
| ------------------------- | -------- | --------------- | --------------- | ------------------ | -------- | ------------------ | --------------------- | -------------------- | ---------------------- | ------------------ | -------------------- | -------------------- | ---------------------- | ---------------------- | ------------------------ | ---------------------------- | ------------------------------ | ---------------------------- | --------------------------------- | ---------------------------------------- | -------------------------------- | -------------------------------------- |
| rbl4_part1_buildable_only | dry_run  | 24              | 23              | 1                  | evosuite | 5                  | 0.2174                | 51.604               | 50.0                   | 55.54              | 52.5                 | 80.572               | 85.71                  | 9.784                  | 10.67                    | 11.2183                      | 2.127                          | 1.0                          | descriptive_only_compiled_n_lt_60 | 0.010235                                 | 0.0                              | 0.413043                               |
| rbl4_part1_buildable_only | dry_run  | 24              | 23              | 1                  | gpt      | 0                  | 0.0                   |                      |                        |                    |                      |                      |                        |                        |                          | 0.0                          | 0.0                            | 1.0                          | descriptive_only_compiled_n_lt_60 | 0.010235                                 | 0.0                              | 0.413043                               |

## Interpretation Guardrails

- GPT compiled-success rows: `0`.
- Do not interpret RQ1/RQ2 as confirmatory if compiled-success rows `< 60`.
- Strict zero-fill metrics are for end-to-end comparison, especially RQ4, not for direct comparison to compiled-only AgoneTest Table IV.
- All skipped rows must be reported as precheck exclusions in the final report.

## Generated Files

- staged_classes: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_staged_classes.csv`
- generation_classes: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_generation_classes.csv`
- baseline_build: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_baseline_build.csv`
- skipped_classes: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_skipped_classes.csv`
- staged_project_info: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_project_info.json`
- environment_checks: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_environment_checks.csv`
- metrics_long: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_metrics_long.csv`
- generated_failures: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_generated_failures.csv`
- summary: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_summary.csv`
- notes: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_notes.md`
- manifest: `results\rbl4_part1_buildable_gpt-4o-mini-2024-07-18_20260714_110634_manifest.json`
