# Data V3 artifact reading and verification guide

This guide describes the snapshot immediately after Step 003. The files
`ARTIFACT_READING_GUIDE.md`, `build_recipes_portable.csv`, and
`BUILD_EVIDENCE_SHA256SUMS.csv` are derived artifacts. They do not replace or
rewrite the original execution history.

## Sealed Step 003 state

- 30 repositories qualified, each at one exact commit.
- All baseline builds used JDK 8 as the effective runtime.
- 678 classes passed structural eligibility and belonged to a passing build
  scope.
- Each qualified repository contributed between 12 and 71 such classes.
- The final 30 repositories contain 48 unique winning build scopes.
- The winning scopes comprise 43 Maven scopes and 5 Gradle scopes.
- No winning recipe modified source, build, or dependency files.
- No winning recipe used an ancillary-skip or external-repository fallback.

The authoritative marker is `state/step003.done.json`. The derived files listed
above must not be described as original outputs recorded by that marker.

## Recommended reading order

| Order | File | Meaning |
|---:|---|---|
| 1 | `state/step003.done.json` | PASS marker, core-output checksums, and repository/class totals. |
| 2 | `successful_repos_manifest.csv` | The final 30 repositories, URLs, exact commits, buildable-class counts, and checkout locations. |
| 3 | `build_recipes.jsonl` | The 48 winning recipes exactly as executed on the creator machine. |
| 4 | `build_recipes_portable.csv` | Derived replay templates with machine-independent placeholders. |
| 5 | `class_metrics_all.csv` | Metrics for every focal class in the final repositories. |
| 6 | `excluded_classes_log.csv` | Classes excluded by structural eligibility or non-passing build scope. |
| 7 | `build_attempts.csv` | Every attempt, including rejected repositories, retries, and resumed work. |
| 8 | `logs/build/*.log` | Raw stdout and stderr from each build attempt. |
| 9 | `BUILD_EVIDENCE_SHA256SUMS.csv` | SHA-256 values for detecting post-seal evidence changes. |

## Understanding `build_attempts.csv`

`build_attempts.csv` is an audit trail, not the final build list. The snapshot
contains 1,422 attempts across 69 repositories that reached the build stage:
1,335 failed attempts and 87 successful attempts. When the target of 30
qualified repositories was reached, the full queue contained 30 qualified, 68
rejected, and 1,150 still-pending repositories.

A scope may fail and later pass because the recipe ladder was declared in
advance:

1. try the module directly;
2. try a registered policy variant;
3. try the Maven reactor with `-pl <module> -am`, or the equivalent Gradle scope;
4. continue to another fallback only for an allowed failure category.

Therefore, a fail-then-pass sequence does not imply that the pipeline edited
code to force a successful build. For example, a Maven module may fail to
resolve a sibling dependency when built alone but pass in a reactor build that
also builds the upstream module.

A repository may contain multiple scopes with eligible focal classes. This is
why the final 30 repositories have 48 winning scopes.

Two qualified repositories contain repeated historical success rows caused by
resume operations:

- `41627638`
- `48046454`

These are audit-history duplicates, not additional winning scopes.
`build_recipes.jsonl` and `build_recipes_portable.csv` contain exactly one recipe
for each `repo_id + scope_key`.

## Understanding eligibility

In `class_metrics_all.csv`, `eligible_for_sampling=True` means only that a class
passed the structural gates. A class belongs to the final sampling frame only
when both conditions hold:

```text
eligible_for_sampling=True
build_scope_pass=True
```

The snapshot contains 1,307 structurally eligible classes, of which 678 also
belong to passing build scopes. Step 004 samples only from this 678-class
intersection.

## `build_recipes_portable.csv` schema

Each row corresponds to one winning recipe in `build_recipes.jsonl`.

| Column | Meaning |
|---|---|
| `recipe_id` | Unique winning-recipe identifier. |
| `repo_url`, `commit_sha` | Source repository and exact commit to check out. |
| `build_tool`, `scope_key` | Build tool and scope proven to pass. |
| `build_root_relative`, `module_dir_relative`, `module_selector` | Build location relative to the repository root. |
| `working_directory_placeholder` | Always `${REPO_DIR}`. |
| `portable_command_windows` | Windows command template. |
| `portable_command_posix` | Linux/macOS command template. |
| `validation_log_relative` | Original passing log path within `data_v3`. |
| `validation_log_sha256` | SHA-256 of the original passing log. |
| `original_command_sha256` | SHA-256 of the UTF-8 absolute command in the original JSONL. |

The portable CSV intentionally excludes creator-machine drive paths.
`${REPO_DIR}` is a literal runner token that must be replaced with the replay
checkout path. It is not automatically a CMD, PowerShell, or Bash environment
variable.

## Replaying one winning recipe

1. Read `repo_url` and `commit_sha` from the selected row.
2. Clone submodules and check out the exact commit:

   ```text
   git clone --recurse-submodules <repo_url> <repo_dir>
   git -C <repo_dir> checkout --detach <commit_sha>
   git -C <repo_dir> submodule update --init --recursive
   ```

3. Use JDK 8. Compare Maven, Gradle, and environment details with
   `results/environment_versions.json`, `state/preflight.json`, and the
   configuration snapshot.
4. Select `portable_command_windows` or `portable_command_posix`.
5. Replace the literal `${REPO_DIR}` with the absolute checkout path and run the
   command from the recorded build root.
6. Do not edit source files, build files, or dependency declarations to obtain
   a pass.
7. Preserve stdout, stderr, exit code, tool versions, and replay time as new
   evidence. Never overwrite the original validation log.

On POSIX systems, a repository wrapper may require execute permission. Granting
execute permission must not change tracked file content; verify the checkout
afterward with `git status --porcelain`.

Winning Maven commands use `clean test-compile`; winning Gradle commands use
`testClasses -x test`. These commands compile baseline main and test sources but
do not execute the full test suite. They must not be reported as “all unit tests
passed.”

A later replay may fail because an external dependency host, DNS, TLS endpoint,
or wrapper distribution is no longer available. Report that event as a replay
failure under the new environment and timestamp. Do not rewrite the original
passing history.

## Verifying sealed evidence

Close Excel or any editor that may hold a CSV open. From PowerShell in the
`data_v3` directory, run:

```powershell
$bad = @()
foreach ($row in Import-Csv .\BUILD_EVIDENCE_SHA256SUMS.csv) {
    $path = Join-Path (Get-Location) ($row.path.Replace('/', '\'))
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) {
        $bad += "$($row.path): MISSING"
        continue
    }
    $actual = (Get-FileHash -LiteralPath $path -Algorithm SHA256).Hash.ToLower()
    if ($actual -ne $row.sha256) {
        $bad += "$($row.path): HASH_MISMATCH"
    }
}
if ($bad.Count -eq 0) {
    "PASS: all sealed evidence matches"
} else {
    $bad
}
```

`BUILD_EVIDENCE_SHA256SUMS.csv` covers:

- the build-attempt audit and deterministic repository queue;
- exact and portable winning recipes;
- manifests, class metrics, and excluded-class evidence;
- the Step 003 marker, configuration snapshots, amendments, and environment
  evidence;
- all build, clone, submodule, and pipeline-step logs present when the snapshot
  was sealed.

The checksum manifest cannot contain its own checksum without creating a
circular reference. If original evidence changes or new logs are added, create
a new versioned checksum manifest. Never overwrite the prior manifest and
present it as the original snapshot.

## Step 004 and Step 005 verification

Step 004 produces exactly 300 main and 60 backup classes through deterministic
seeded hashing. Seed 42 is part of the SHA-256 input; SHA-256 provides a stable
ordering and does not replace the seed. Complexity does not influence selection.

Step 005 reconstructs the selection and verifies:

- 30 × (10 main + 2 backup);
- uniqueness and zero main/backup overlap;
- exact focal paths and the effective JDK 8 runtime;
- two relative complexity halves of 150 classes each.

Only after all checks pass does the pipeline create `results/RUN_READY` and:

- `results/validation_report.md`
- `results/sampling_methodology.md`
- `results/data_integrity_report.md`
- `results/SHA256SUMS.csv`

Do not start GPT or EvoSuite experiments without `RUN_READY`.
