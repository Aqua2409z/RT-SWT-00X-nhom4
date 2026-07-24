# Legacy sampling snapshot

Các file trong thư mục này được chuyển nguyên trạng từ `data/` để tránh bị hiểu
nhầm là kết quả Data V3.

Snapshot lịch sử này báo cáo 33 repository, 300 main class và 58 backup class.
Nó còn chứa lệnh build với đường dẫn tuyệt đối của máy thu thập dữ liệu và một
số dòng pilot ở trạng thái `PENDING`. Phương pháp lấy mẫu cũng dùng CC làm biến
eligibility/phân bổ, khác với protocol V3 đã đóng băng.

Không dùng snapshot này cho:

- input của GPT hoặc EvoSuite;
- thống kê dataset trong paper hiện tại;
- tái build 30 repository V3;
- thay thế manifest chính thức tại `../../v3/`.

Việc lưu snapshot trong Git chỉ nhằm bảo toàn provenance và khả năng giải thích
sự thay đổi phương pháp giữa các vòng nghiên cứu.
