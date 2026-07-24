# Data V3 construction pipeline

This directory preserves the source used to create and validate Data V3. Run
the entry points in this order:

```text
000_preflight_and_backup.py
001_reconstruct_unique_frame.py
002_create_repo_queue.py
003_screen_and_build_repositories.py
004_sample_classes.py
005_validate_and_report.py
```

`v3_core.py` contains the active implementation. `pipeline_v3.py` is retained
because it belongs to the sealed source inventory, but it is not the recommended
entry point.

## Frozen and example configurations

`config_v3.yaml` is the exact creator-machine configuration. It remains
unchanged so its checksum in `data/v3/results/SHA256SUMS.csv` can be verified.
Because it contains historical absolute paths, it must not be executed on
another machine.

To reconstruct the dataset independently:

1. Copy `config_v3.example.yaml` to an untracked local configuration.
2. Run commands from `scripts/data_pipeline_v3`.
3. Configure local paths to CLASSES2TEST, V2 evidence, and a complete JDK 8.
4. Use a new output directory; never overwrite `data/v3`.

Non-production checks:

```bat
py -m py_compile v3_core.py 000_preflight_and_backup.py 001_reconstruct_unique_frame.py 002_create_repo_queue.py 003_screen_and_build_repositories.py 004_sample_classes.py 005_validate_and_report.py
py 003_screen_and_build_repositories.py --help
py -m unittest tests.test_v3_fixtures
```

Dataset construction is separated from GPT, EvoSuite, JaCoCo, and PIT.
Downstream experimental outcomes never influence repository or class selection.
