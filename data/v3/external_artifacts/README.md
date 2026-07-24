# External Data V3 artifacts

Các binary artifact dung lượng lớn không được commit vào GitHub.

## Gói đã tạo

`DELIVERY_MANIFEST.json` và `DELIVERY_SHA256SUMS.csv` mô tả gói
`V3_BUILD_HANDOFF_20260723_r1`, gồm:

- `V3_BUILD_BUNDLE.tar.gz`;
- `classes2test-v3-build-handoff-1.0.tar`;
- hướng dẫn import/chạy;
- trình xác minh delivery.

`verify_delivery.py` chỉ kiểm kê manifest, checksum và thành viên archive; nó
không Maven/Gradle build 30 repository.

## Trạng thái khoa học

Manifest delivery đang ghi `CANDIDATE_NOT_CONFIRMED_48_OF_48`. Trạng thái này
phải được giữ nguyên cho đến khi replay đủ 48 recipe dưới đúng fingerprint môi
trường và tất cả đều PASS. Không đổi nhãn thành “confirmed” chỉ vì bước đóng gói
hoặc `--check-only` đã PASS.

## Vị trí tải

URL lưu trữ archive chưa được công bố trong repository. Khi nhóm chọn Google
Drive, OneDrive, OSF, Zenodo hoặc GitHub Release phù hợp, bổ sung:

- URL cố định;
- ngày upload;
- dung lượng;
- SHA-256 trùng `DELIVERY_SHA256SUMS.csv`;
- quyền truy cập và phiên bản artifact.

Không commit trực tiếp file TAR/ZIP hoặc 30 repository của bên thứ ba vào branch
`main`.
