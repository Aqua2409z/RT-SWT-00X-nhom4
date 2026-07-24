# Data V3 build handoff

Đây là bản public của runner dùng để kiểm tra và replay 48 build recipe trên 30
repository Data V3. Runner không chạy GPT, EvoSuite, JaCoCo hoặc PIT.

Tài liệu chi tiết, bao gồm resume và ý nghĩa kết quả, nằm trong
[`README_BUILD_HANDOFF.md`](README_BUILD_HANDOFF.md).

## Nội dung GitHub và full delivery bundle

GitHub chứa source runner, manifest và checksum nhưng không chứa
`data/v3/repos/successful`. Vì vậy:

- `py scripts/verify_bundle.py` chỉ PASS đầy đủ khi đang trỏ đến full delivery
  bundle đã giải nén;
- `replay_builds.py --check-only` không build, nhưng vẫn cần repository/commit
  để kiểm tra nguồn;
- `--all --resume --jobs 1` mới replay toàn bộ build và có thể chạy nhiều giờ.

Không coi `docker compose run --rm build-handoff` là replay 30 repository. Lệnh
mặc định chỉ gọi `--check-only`.

## Chạy từ checkout GitHub

1. Tải và xác minh full delivery bundle theo
   `../../data/v3/external_artifacts/`.
2. Đặt/copy 30 repository đóng băng vào
   `data/v3/repos/successful/<repo_id>` và các validation logs vào đúng đường
   dẫn manifest, hoặc chạy runner trực tiếp trong layout của bundle.
3. Từ thư mục này:

```bat
docker compose build
docker compose run --rm build-handoff
docker compose run --rm build-handoff python3 scripts/replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

Mặc định dùng `jobs=1` để tránh RAM tràn và nhiều Gradle daemon.
