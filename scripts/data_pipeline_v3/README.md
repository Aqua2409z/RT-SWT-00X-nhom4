# Data V3 construction pipeline

Thư mục này lưu source đã dùng để tạo và xác nhận Data V3. Các entry point phải
chạy tuần tự:

```text
000_preflight_and_backup.py
001_reconstruct_unique_frame.py
002_create_repo_queue.py
003_screen_and_build_repositories.py
004_sample_classes.py
005_validate_and_report.py
```

`v3_core.py` chứa implementation chính. `pipeline_v3.py` được giữ vì nó nằm
trong source inventory đã niêm phong, không phải entry point khuyến nghị.

## Frozen config và config mẫu

`config_v3.yaml` là config gốc đã chạy trên máy dựng dữ liệu và được giữ nguyên
để checksum trong `data/v3/results/SHA256SUMS.csv` còn kiểm chứng được. Nó chứa
đường dẫn tuyệt đối lịch sử nên không chạy trực tiếp trên máy khác.

Muốn tái dựng từ đầu:

1. Copy `config_v3.example.yaml` thành một file config cục bộ không commit.
2. Chạy lệnh từ chính thư mục `scripts/data_pipeline_v3`.
3. Điền đường dẫn đến CLASSES2TEST, V2 evidence và JDK 8 của máy đó.
4. Không sửa `data/v3`; dùng một output directory mới.

Ví dụ kiểm tra không chạy dữ liệu thật:

```bat
py -m py_compile v3_core.py 000_preflight_and_backup.py 001_reconstruct_unique_frame.py 002_create_repo_queue.py 003_screen_and_build_repositories.py 004_sample_classes.py 005_validate_and_report.py
py 003_screen_and_build_repositories.py --help
py -m unittest tests.test_v3_fixtures
```

Pipeline dựng dataset tách biệt với GPT/EvoSuite/JaCoCo/PIT. Không đưa outcome
thực nghiệm vào quyết định repository hoặc class.
