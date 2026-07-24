# Data V3 — canonical pre-experiment dataset

Đây là bộ artifact Data V3 đã được pipeline phiên bản
`3.1.0-pre-experiment` xác nhận trước khi chạy GPT, EvoSuite, JaCoCo hoặc PIT.
Không sửa trực tiếp các CSV/JSON đã niêm phong.

## File nào dùng cho việc gì?

| Mục đích | File chính thức |
|---|---|
| Danh sách 30 repository và exact commit | `successful_repos_manifest.csv` |
| 300 focal class chính | `class_sampling_manifest_final_seed42.csv` |
| 60 focal class dự phòng | `class_backup_manifest_seed42.csv` |
| Kiểm tra 10 main + 2 backup mỗi repo | `repo_sampling_summary.csv` |
| Lệnh build có thể chuyển máy | `build_recipes_portable.csv` |
| Lịch sử mọi build attempt | `build_attempts.csv` |
| Checksum attempts và validation logs | `BUILD_EVIDENCE_SHA256SUMS.csv` |
| Báo cáo invariant cuối | `results/validation_report.md` |
| Biện pháp bảo vệ liêm chính | `results/data_integrity_report.md` |
| Thuật toán lấy mẫu | `results/sampling_methodology.md` |
| Phân bố hai nửa CC | `results/complexity_halves_summary.json` |
| Môi trường ghi nhận | `results/environment_versions.json` |
| Trạng thái đóng băng | `results/RUN_READY` |
| Checksum source/output chính | `results/SHA256SUMS.csv` |

`class_sampling_manifest_seed42.csv` là đầu ra trước bước gắn nhãn complexity
half. Thực nghiệm phải dùng `class_sampling_manifest_final_seed42.csv`.

## Protocol lấy mẫu

Đơn vị là một physical focal Java source file, định danh bằng `repo_id` và
normalized focal path. Repository được xét theo hàng đợi tất định từ seed 42 và
SHA-256. Ba mươi repository đạt chuẩn đầu tiên đóng góp đúng 10 main class và 2
backup class.

Cyclomatic Complexity không tham gia eligibility hay selection. Sau khi chọn
xong 300 main class, pipeline mới xếp hạng theo `max_method_cc` rồi `sum_method_cc`
để chia hai nửa tương đối 150/150. Vì vậy nhãn `lower_complexity_half` và
`higher_complexity_half` không phải ngưỡng CC tuyệt đối.

Backup chỉ được dùng khi có lỗi kỹ thuật trước khi đo outcome và phải ghi lại
`replacement_of` cùng `replacement_reason`. Không được thay class vì GPT hoặc
EvoSuite cho kết quả không thuận lợi.

## Cách đọc build evidence

`build_attempts.csv` là bằng chứng lịch sử bất biến. Cột `command` và
`working_directory` có thể chứa đường dẫn tuyệt đối của máy dựng dữ liệu; không
copy chúng để chạy trên máy khác.

Để tái build, dùng `build_recipes_portable.csv`. Runner sẽ thay `${REPO_DIR}`
bằng workspace writable thực tế và chọn cột Windows hoặc POSIX tương ứng.

## Nội dung không nằm trong Git

Git không chứa:

- `repos/successful/` với 30 repository đóng băng;
- 1.567 validation/build logs;
- `raw_mapping_index.csv` và `unique_focal_class_frame.csv`;
- `V3_BUILD_BUNDLE.tar.gz`, ZIP delivery hoặc Docker image TAR;
- cache Maven/Gradle và build output.

Các file trên thuộc gói chuyển giao dung lượng lớn. Metadata và checksum của gói
nằm tại [`external_artifacts/`](external_artifacts/). Sau khi nhận archive, luôn
chạy `verify_delivery.py` trước khi giải nén/chạy build.

## Kiểm tra nhanh số lượng

Việc kiểm tra CSV không build repository:

```bat
py ..\..\scripts\data_pipeline_v3\005_validate_and_report.py --help
```

Để kiểm tra/tái build từ full delivery bundle, xem:
[`../../scripts/build_handoff_v3/README.md`](../../scripts/build_handoff_v3/README.md).
