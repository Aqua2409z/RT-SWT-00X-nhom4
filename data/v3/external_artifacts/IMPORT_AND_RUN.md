# Import and run Data V3 build handoff

This delivery is a Docker portability candidate. It is not declared 48/48
validated until the receiving machine completes all frozen recipes.

## 1. Verify the delivery

```bat
py verify_delivery.py .
```

## 2. Load and tag the exact exported image

```bat
docker load -i "classes2test-v3-build-handoff-1.0.tar"
docker tag "sha256:6f413e138c6cb482728b7e15a97f1d30bdae5a38da95ed828dd37058719f8862" "classes2test-v3-build-handoff:1.0"
docker image inspect "sha256:6f413e138c6cb482728b7e15a97f1d30bdae5a38da95ed828dd37058719f8862"
```

Tagging by the recorded immutable ID also works around Docker Desktop versions
that list a tag but cannot resolve it through `docker image inspect <tag>`.
Do not rebuild the image under the same delivery revision.


## 3. Extract the frozen bundle

```bat
tar.exe -xzf "V3_BUILD_BUNDLE.tar.gz"
cd "V3_BUILD_BUNDLE\v3_build_handoff"
```

## 4. Read-only preflight

```bat
docker compose run --rm --no-build build-handoff
```

## 5. Smoke test

```bat
docker compose run --rm --no-build build-handoff python3 scripts/replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

## 6. Full independent replay

```bat
docker compose run --rm --no-build build-handoff python3 scripts/replay_builds.py --config handoff_config.json --all --resume --jobs 1
```

Keep `build_replay_results` from this machine separate from the creator's
results. Never edit the frozen `data_v3` or Step 001-005 evidence.
