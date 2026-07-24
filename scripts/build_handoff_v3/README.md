# Data V3 build handoff

This is the public runner for verifying and replaying 48 build recipes across
the 30 Data V3 repositories. It does not run GPT, EvoSuite, JaCoCo, or PIT.

Detailed setup, resume, acceptance, and output semantics are documented in
[`README_BUILD_HANDOFF.md`](README_BUILD_HANDOFF.md).

## Git checkout versus full delivery

Git contains the runner source, manifests, and checksums, but it does not contain
`data/v3/repos/successful`. Therefore:

- `verify_bundle.py` can fully pass only against an extracted delivery bundle;
- `replay_builds.py --check-only` does not build, but it still needs repository
  checkouts to validate commits and commands;
- `--all --resume --jobs 1` performs the full replay and may take several hours.

The default `docker compose run --rm build-handoff` command invokes only
`--check-only`; it does not replay all 30 repositories.

## Running from this repository

1. Download and verify the full delivery described under
   `../../data/v3/external_artifacts/`.
2. Place the 30 frozen checkouts under
   `data/v3/repos/successful/<repo_id>` and the validation logs at their recorded
   paths, or run the handoff directly from the extracted bundle layout.
3. From this directory, run:

```bat
docker compose build
docker compose run --rm build-handoff
docker compose run --rm build-handoff python3 scripts/replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

Keep `jobs=1` for the first full replay to avoid memory exhaustion and excessive
Gradle daemons.
