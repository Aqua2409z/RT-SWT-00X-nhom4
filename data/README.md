# Research data index

This directory separates the active research dataset from historical data.

## Canonical dataset

[`v3/`](v3/) is the Data V3 release used by the current paper and experiments.
Step 005 validated the following state:

- 30 repositories at exact commits;
- 300 main focal classes, exactly 10 per repository;
- 60 backup focal classes, exactly 2 per repository;
- zero overlap between the main and backup samples;
- 150 classes in the lower relative-complexity half and 150 in the higher half;
- 48 portable build recipes covering all 30 repositories;
- JDK 8 as the effective screening runtime.

Start with [`v3/README.md`](v3/README.md).

## Historical data

[`legacy/v2_week_previous/`](legacy/v2_week_previous/) preserves the previous
sampling snapshot. That snapshot contains 33 repositories, 300 main classes,
58 backup classes, and a CC-balancing policy that is incompatible with V3.

Do not use files under `legacy/` as experimental inputs or as evidence for the
Data V3 paper. They are retained only for research provenance.

## Data and software separation

- Data V3 construction source:
  [`../scripts/data_pipeline_v3/`](../scripts/data_pipeline_v3/)
- Build verification and replay tools:
  [`../scripts/build_handoff_v3/`](../scripts/build_handoff_v3/)
- Frozen repositories, Docker images, and multi-gigabyte archives are not stored
  in Git. See [`v3/external_artifacts/`](v3/external_artifacts/) for delivery
  metadata and checksums.
