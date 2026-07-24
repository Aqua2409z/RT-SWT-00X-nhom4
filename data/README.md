# Research data index

Thư mục này phân tách rõ dữ liệu đang dùng cho nghiên cứu và dữ liệu lịch sử.

## Bộ dữ liệu chính thức

[`v3/`](v3/) là bộ Data V3 dùng cho paper và thực nghiệm hiện tại. Trạng thái đã
được Step 005 xác nhận:

- 30 repository tại exact commit;
- 300 focal class chính, đúng 10 class mỗi repository;
- 60 focal class dự phòng, đúng 2 class mỗi repository;
- main/backup không trùng nhau;
- 150 class thuộc nửa độ phức tạp tương đối thấp và 150 class thuộc nửa tương
  đối cao;
- 48 build recipe portable phủ đủ 30 repository;
- effective runtime của vòng sàng lọc là JDK 8.

Điểm bắt đầu để đọc bộ dữ liệu là [`v3/README.md`](v3/README.md).

## Dữ liệu lịch sử

[`legacy/v2_week_previous/`](legacy/v2_week_previous/) lưu snapshot từ vòng lấy
mẫu trước. Snapshot này có 33 repository, 300 main class và 58 backup class,
đồng thời dùng chiến lược cân bằng CC khác với V3.

Không dùng file trong `legacy/` để chạy thực nghiệm hoặc trích số liệu cho paper
Data V3. Các file được giữ lại chỉ để bảo toàn lịch sử nghiên cứu.

## Tách biệt dữ liệu và chương trình

- Mã dựng Data V3: [`../scripts/data_pipeline_v3/`](../scripts/data_pipeline_v3/)
- Công cụ kiểm tra/tái build:
  [`../scripts/build_handoff_v3/`](../scripts/build_handoff_v3/)
- Các repository đóng băng, Docker image và archive nhiều GB không được commit
  vào Git. Xem [`v3/external_artifacts/`](v3/external_artifacts/) để xác minh gói
  chuyển giao.
