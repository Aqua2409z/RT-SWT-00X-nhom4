# IE Criteria – LLM for Unit Test Case Generation
**Thành viên:** Trần Minh Quý

**RQ:** "Đối với Java/Python functions ở cyclomatic complexity trung bình, GPT-4 tự động sinh unit test cases so với test cases viết thủ công bởi sinh viên có đạt branch coverage ≥ 80% và mutation score ≥ 60% không?"

**PICO:** P=[Java/Python functions có cyclomatic complexity trung bình (CC 5–15)] | I=[GPT-4 tự động sinh unit test cases] | C=[Unit test cases viết thủ công bởi sinh viên và Randoop] | O=[Branch Coverage ≥ 80% và Mutation Score ≥ 60%]

---

## Inclusion Criteria (IC) – paper PHẢI có đủ tất cả

| Mã | Tiêu chí |
|----|----------|
| **IC-L** | Viết bằng tiếng Anh |
| **IC-Y** | Xuất bản từ 2020 đến nay – Lý do: Năm 2020 là cột mốc bùng nổ của các mô hình ngôn ngữ lớn (LLM), đảm bảo các nghiên cứu thu thập được có tính cập nhật và liên quan trực tiếp đến công nghệ Generative AI. |
| **IC-T** | Đăng trên conference hoặc journal – không phải blog, thesis, hay báo cáo kỹ thuật |
| **IC-P** | Về task: Kiểm thử đơn vị (Unit Testing) cho các hàm/chức năng viết bằng Java hoặc Python có độ phức tạp vòng (cyclomatic complexity) ở mức trung bình. |
| **IC-I** | Dùng kỹ thuật: Tự động sinh test case sử dụng mô hình ngôn ngữ lớn (LLM), bao gồm các mô hình Generative AI thế hệ mới nói chung và GPT-4 nói riêng. |
| **IC-E** | Có ít nhất 1 con số kết quả trong Table hoặc Figure của paper gốc |

## Exclusion Criteria (EC) – loại nếu BẤT KỲ điều kiện nào đúng

| Mã | Tiêu chí |
|----|----------|
| **EC-D** | Trùng lặp với paper đã có trong danh sách |
| **EC-A** | Không truy cập được full-text |
| **EC-S** | Dưới 4 trang (extended abstract, poster, short paper) |
| **EC-N** | Không có thực nghiệm (position paper, vision paper, tutorial) |
| **EC-O** | Không về topic: Các nghiên cứu về sinh mã nguồn tổng quát (General Code Generation), sửa lỗi chương trình tự động (APR), kiểm thử mức hệ thống/giao diện (System/GUI Testing), hoặc kiểm thử truyền thống không đề cập đến LLM. |