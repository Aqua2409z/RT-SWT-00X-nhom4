# Data V3 Build Handoff

This directory contains the reproducibility tools used to verify and replay the build recipes distributed with Data V3. It is intentionally separate from the frozen dataset-construction pipeline.

The handoff runner:

- verifies the delivery manifests and relative repository paths;
- replays the 48 recorded build recipes across the 30 selected repositories;
- records stdout, stderr, exit codes, durations, environment fingerprints, and source-tree integrity checks;
- never runs GPT, EvoSuite, JaCoCo, or PIT.

The default concurrency is one repository at a time to limit memory pressure and avoid concurrent Gradle daemons.

## 1. Scope and reproducibility principles

The Data V3 construction outputs are frozen. Repository snapshots are read from:

```text
data_v3/repos/successful/<repo_id>
```

The runner never builds directly inside those frozen directories. It creates writable copies under `build_work`, verifies each copied repository against its recorded commit, runs the selected recipe, and writes replay evidence under `build_replay_results`.

Historical absolute paths in `build_attempts.csv` and processing logs are preserved as immutable provenance. They are not portable commands. Use `build_recipes_portable.csv` and this runner for replay.

## 2. Required delivery layout

After extracting the delivery archive, the following relative layout must be preserved:

```text
V3_HANDOFF/
├── data_v3/
│   ├── successful_repos_manifest.csv
│   ├── class_sampling_manifest_final_seed42.csv
│   ├── class_backup_manifest_seed42.csv
│   ├── build_recipes_portable.csv
│   └── repos/
│       └── successful/
├── research_pipeline_v3/
└── v3_build_handoff/
    ├── Dockerfile
    ├── compose.yaml
    ├── handoff_config.json
    ├── README_BUILD_HANDOFF.md
    ├── native_windows/
    ├── scripts/
    └── output/
```

For a tar archive:

```cmd
tar -czf V3_BUILD_BUNDLE.tar.gz data_v3 research_pipeline_v3 v3_build_handoff
```

For a ZIP archive in PowerShell:

```powershell
Compress-Archive -Path data_v3,research_pipeline_v3,v3_build_handoff -DestinationPath V3_BUILD_BUNDLE.zip
```

Do not flatten the directory structure. The configuration and Compose mounts rely on these relative paths.

## 3. Relative configuration

`handoff_config.json` contains no machine-specific absolute path:

```json
{
  "data_root": "../data_v3",
  "repository_manifest": "successful_repos_manifest.csv",
  "final_sample_manifest": "class_sampling_manifest_final_seed42.csv",
  "backup_manifest": "class_backup_manifest_seed42.csv",
  "portable_recipes": "build_recipes_portable.csv",
  "repository_storage_root": "repos/successful",
  "work_root": "../build_work",
  "result_root": "../build_replay_results",
  "default_jobs": 1
}
```

For each recipe, the runner resolves:

```text
REPO_DIR = <data_root>/<repository_storage_path>
```

It then replaces the literal placeholder `${REPO_DIR}` in the portable command with the repository's writable workspace path. For example:

```text
mvn -B -ntp -DskipTests -DskipITs -f "${REPO_DIR}/pom.xml" -pl contrib/flo-bigquery -am clean test-compile
```

No user needs to edit a command to insert a Windows or Linux path.

## 4. Verify the bundle without building repositories

From `v3_build_handoff`:

```cmd
py scripts\verify_bundle.py --config handoff_config.json
```

On Linux or inside the container:

```sh
python3 scripts/verify_bundle.py --config handoff_config.json
```

The verifier checks:

- the required manifests exist;
- the final repository set contains exactly 30 repositories;
- the main sample contains 300 classes and the backup sample contains 60 classes;
- each repository contributes 10 main and 2 backup classes;
- main and backup samples do not overlap;
- the main sample contains 150 lower-CC and 150 higher-CC classes;
- every portable recipe belongs to a selected repository;
- each repository snapshot and recorded commit are available.

This command reads manifests and repository metadata only. It does not compile the repositories.

The runner also provides a build-free validation mode:

```cmd
py scripts\replay_builds.py --config handoff_config.json --check-only
```

## 5. Native Windows replay

### Requirements

- a 64-bit JDK 8, including both `java.exe` and `javac.exe`;
- Maven available as `mvn`;
- Git available as `git`;
- Python 3;
- repository-provided Gradle wrappers for Gradle projects.

Do not use a standalone JRE. The compiler is required.

Open a new `cmd.exe` window and run:

```cmd
native_windows\setup_jdk8.cmd
native_windows\preflight.cmd
```

If JDK 8 is installed elsewhere, set it explicitly before running the preflight:

```cmd
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202"
set "PATH=%JAVA_HOME%\bin;%PATH%"
native_windows\preflight.cmd
```

Verify one recipe:

```cmd
py scripts\replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

Verify all recipes sequentially and resume from existing PASS results:

```cmd
py scripts\replay_builds.py --config handoff_config.json --all --resume --jobs 1
```

Additional selection modes:

```cmd
py scripts\replay_builds.py --config handoff_config.json --repo-id 46450575
py scripts\replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

## 6. Docker replay

The Dockerfile extends the previously validated V2 environment:

```text
minhquy266/classes2test-pipeline:pilot-v1
```

The handoff image adds the replay scripts and configures Java 8, Maven, Git, Python, and conservative Maven/Gradle runtime options. The repository's own Maven or Gradle wrapper remains responsible for the project-specific build-tool version.

Build the handoff image:

```cmd
docker compose build
```

Run a fast environment and manifest check without compiling repositories:

```cmd
docker compose run --rm build-handoff
```

The default Compose command is `--check-only`.

Replay one recipe:

```cmd
docker compose run --rm build-handoff python3 scripts/replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

Replay all recipes sequentially, skipping prior PASS results produced under the same environment fingerprint:

```cmd
docker compose run --rm build-handoff python3 scripts/replay_builds.py --config handoff_config.json --all --resume --jobs 1
```

When using a separately distributed image archive:

```cmd
docker load -i classes2test-v3-build-handoff-1.0.tar
docker tag <loaded-image-id> classes2test-v3-build-handoff:1.0
docker compose run --rm build-handoff
```

`docker compose run` does not accept `--no-build` on every Compose version. If the image has already been loaded or built, Compose reuses it automatically unless the configuration explicitly requests another build.

## 7. Replay flow and recorded evidence

For each selected repository, the runner:

1. confirms that the repository belongs to the final 30-repository manifest;
2. reads the exact `commit_sha`;
3. copies the frozen repository to a writable workspace;
4. verifies `HEAD == commit_sha`;
5. selects the POSIX or Windows portable command;
6. checks Java, `javac`, Maven, or the repository's Gradle wrapper;
7. replaces `${REPO_DIR}` with the writable workspace path;
8. executes the command;
9. stores stdout, stderr, exit code, duration, and environment data;
10. verifies that tracked source files were not changed by the build;
11. appends the result to `build_replay_summary.csv`.

Expected output layout:

```text
build_replay_results/
├── build_replay_summary.csv
├── environment.json
└── logs/
    └── <recipe-id>/
        ├── stdout.log
        ├── stderr.log
        └── result.json
```

A replay is considered PASS only when:

- the command exits with code 0;
- the checked-out commit matches the manifest;
- tracked files remain unchanged after the build;
- the result is written successfully.

Build-generated untracked files such as `target/`, `build/`, and Gradle caches are allowed inside the writable workspace and never modify the frozen dataset.

## 8. Team acceptance workflow

Recommended acceptance sequence:

1. Extract the delivery archive without changing its directory hierarchy.
2. Run `verify_bundle.py`.
3. Load the supplied Docker image or build it from the Dockerfile.
4. Run the default Docker check-only command.
5. Replay one named recipe.
6. Inspect its logs and `build_replay_summary.csv`.
7. Run `--all --resume --jobs 1` when time and resources permit.
8. Preserve the result directory as the team's independent build-replay evidence.

Do not claim that all 48 recipes were independently reproduced until the summary contains 48 PASS records under the intended environment. A delivery status such as `CANDIDATE_NOT_CONFIRMED_48_OF_48` means that the package structure passed verification, not that every build was replayed successfully.

## 9. Size, caches, and resource controls

- The repository snapshots are included in the delivery bundle; the recipient does not need to clone them again.
- Maven and Gradle may download dependencies during first use unless dependency caches are distributed separately.
- The default `--jobs 1` setting is deliberate. Increase concurrency only after observing memory and CPU use.
- Gradle recipes use `--no-daemon` where recorded to reduce persistent JVM memory.
- `--resume` skips only prior PASS results produced under the same environment fingerprint.
- `build_work` and `build_replay_results` are generated handoff outputs and are not part of the frozen Data V3 inventory.
