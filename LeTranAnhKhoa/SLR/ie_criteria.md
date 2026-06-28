# IE Criteria - LLM for Unit Test Case Generation
**Thành viên:** Lê Trần Anh Khoa
**RQ:** "Hiệu quả tự động sinh unit test cases của GPT-4 cho các hàm Java/Python có độ phức tạp trung bình so với viết thủ công bởi sinh viên đạt mức độ bao nhiêu xét theo Branch Coverage và Mutation Score?"
**PICO:** P=[Java/Python functions có cyclomatic complexity trung bình] | I=[GPT-4 tự động sinh unit test cases] | C=[Unit test cases viết thủ công bởi sinh viên] | O=[Branch Coverage ≥ 80%, Mutation Score ≥ 60%]

---
## Inclusion Criteria (IC) – paper PHẢI có đủ tất cả
Mã	    Tiêu chí
IC-L	Viết bằng tiếng Anh

IC-Y	Xuất bản từ 2025 đến nay – Lý do: Dòng mô hình GPT-4 và các ứng dụng nâng cao của LLM trong kiểm thử phần   mềm tự động bùng nổ, tối ưu hóa mạnh mẽ từ năm 2024.

IC-T	Đăng trên conference hoặc journal – không phải blog, thesis, preprint chưa peer-review, hay báo cáo kỹ thuật

IC-P    Sinh unit test case tự động từ mã nguồn hàm/chương trình viết bằng Java hoặc Python.

IC-I	Dùng kỹ thuật: Mô hình ngôn ngữ lớn (LLM), trọng tâm là GPT-4 hoặc ChatGPT có nền tảng GPT-4. Nếu paper dùng nhiều LLM (ví dụ so sánh GPT-3.5, GPT-4, Llama), bắt buộc phải có kết quả đánh giá riêng cho GPT-4.

IC-E    Có ít nhất một con số về Branch Coverage hoặc Mutation Score trong Table/Figure của paper gốc (Không chấp nhận Line/Statement Coverage thay thế, vì không tương đương trong bối cảnh RQ).

## Exclusion Criteria (EC) – loại nếu BẤT KỲ điều kiện nào đúng
Mã	Tiêu chí (sau chỉnh sửa)
EC-D	Trùng lặp với paper đã có trong danh sách (cùng DOI hoặc cùng tiêu đề và tác giả).

EC-A	Không truy cập được full-text sau khi đã thử qua các nguồn hợp pháp (thư viện trường, liên hệ tác giả, open access).

EC-S	Dưới 4 trang (extended abstract, poster, short paper).

EC-N	Không có thực nghiệm (position paper, vision paper, tutorial, systematic literature review thuần túy không có thí nghiệm).

EC-O	Không về topic: Các task dễ nhầm lẫn như thực thi test (test execution), tìm và sửa lỗi (debugging /  automated program repair), bảo trì test case (test maintenance), UI testing, integration testing, acceptance testing, hoặc sinh mã nguồn tổng quát (general code generation) không nhằm mục đích tạo unit test.