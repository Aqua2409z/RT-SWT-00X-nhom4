# IE Criteria – LLM for Unit Test Case Generation
**Thành viên:** Trần Bích Trâm
**RQ:** "Mức độ chênh lệch về Branch Coverage và Mutation Score giữa mã nguồn sinh bởi GPT-4 và mã nguồn viết thủ công bởi sinh viên là bao nhiêu, và liệu GPT-4 có thể vượt qua ngưỡng mục tiêu (80% coverage / 60% mutation score) một cách nhất quán không?"
**PICO:** P= Mã nguồn (Source Code) của sinh viên trong các khóa học lập trình hoặc các dự án phần mềm có quy mô nhỏ/trung bình. | I= Sử dụng GPT-4 để tự động sinh mã nguồn (Code Generation) hoặc sinh test case (Test Generation). | C= Mã nguồn/Test case được viết thủ công bởi sinh viên (Manual Writing). | O= Mức độ bao phủ nhánh (Branch Coverage) và khả năng phát hiện lỗi qua kiểm thử đột biến (Mutation Score).

---

## Inclusion Criteria (IC) – paper PHẢI có đủ tất cả

| Mã | Tiêu chí |
| :--- | :--- |
| **IC-L** | Viết bằng tiếng Anh |
| **IC-Y** | Xuất bản từ 2023 đến nay – Lý do: GPT-4 chính thức ra mắt vào năm 2023, các nghiên cứu trước đó không có mô hình này. |
| **IC-T** | Đăng trên conference hoặc journal – không phải blog, thesis, hay báo cáo kỹ thuật |
| **IC-P** | Về task: Sinh unit test tự động cho các hàm Java/Python (có độ phức tạp cyclomatic trung bình) |
| **IC-I** | Dùng kỹ thuật: Ứng dụng mô hình GPT-4 (bao gồm các kỹ thuật zero-shot, few-shot hoặc prompt engineering liên quan đến GPT-4). |
| **IC-E** | Có ít nhất 1 con số kết quả trong Table hoặc Figure của paper gốc |

## Exclusion Criteria (EC) – loại nếu BẤT KỲ điều kiện nào đúng

| Mã | Tiêu chí |
| :--- | :--- |
| **EC-D** | Trùng lặp với paper đã có trong danh sách |
| **EC-A** | Không truy cập được full-text |
| **EC-S** | Dưới 4 trang (extended abstract, poster, short paper) |
| **EC-N** | Không có thực nghiệm (position paper, vision paper, tutorial) |
| **EC-O** | Không về topic: Các bài toán dễ bị nhầm lẫn với sinh unit test như: (1) Sinh mã nguồn ứng dụng (Code Generation), (2) Sửa lỗi code tự động (Automated Program Repair), hoặc (3) Sinh dữ liệu test nhưng cho ngôn ngữ khác ngoài Java/Python. |