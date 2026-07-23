# Danh sách GAP nghiên cứu tiềm năng (gap-list.md)

| # | Mô tả GAP | Loại | Bằng chứng từ merged evidence table | Người chọn |
|:---|:---|:---|:---|:---|
| **G1** | Thiếu đánh giá chuyên sâu và so sánh trực tiếp GPT-4 / GPT-4o đồng thời với cả kiểm thử thủ công (human-written tests) và các công cụ sinh test tự động truyền thống (EvoSuite/Randoop) dưới cùng một điều kiện thực nghiệm. | **GAP-T** | Cột Tool/LLM chủ yếu dùng GPT-3.5-turbo hoặc các mô hình mã nguồn mở đơn lẻ; rất ít bài so sánh trực tiếp GPT-4 đồng thời với cả con người và EvoSuite trên cùng một benchmark. | Trần Minh Quý, Vương Quốc Khánh, Lê Trần Anh Khoa |
| **G2** | Thiếu so sánh chuẩn hóa giữa 2 LLM trở lên trên cả hai ngôn ngữ Java và Python song song với cùng một bộ đối tượng và cùng giao thức đo lường. | **GAP-T** | Hầu hết các nghiên cứu giới hạn ở 1 ngôn ngữ duy nhất (Java only: HITS, CANDOR, RefTest; Python only: Benchmark, CoverUp). | Trương Đan Huy, Lê Trần Anh Khoa, Trần Bích Trâm |
| **G3** | Thiếu sự tích hợp sâu giữa phân tích chương trình nâng cao và LLM để giải quyết triệt để lỗi sinh phụ thuộc giả (fabricated dependencies), đệ quy hoặc vòng lặp. | **GAP-T** | Các công cụ như TestPilot, TestCTRL, TELPA, CANDOR vẫn phụ thuộc nặng nề vào cơ chế thử-sai (trial-and-error) hoặc gặp hạn chế lớn với cấu trúc phức tạp. | Trần Bích Trâm |
| **G4** | Thiếu nghiên cứu đánh giá đồng thời cả độ bao phủ (Branch/Line Coverage) và khả năng phát hiện lỗi thực tế (Mutation Score) một cách nhất quán trên toàn bộ bộ test được sinh ra (thay vì chỉ tính trên green suite). | **GAP-M** | Hầu hết các bài báo chỉ đo Coverage hoặc chỉ đo Mutation Score. Các bài báo cáo cả hai như AgoneTest lại chỉ tính Mutation Score trên các test case biên dịch thành công (green suite - chiếm 30-38%), không tính trên toàn bộ test sinh ra. | Trương Đan Huy, Trần Minh Quý, Vương Quốc Khánh, Trần Bích Trâm |
| **G5** | Thiếu các thước đo về chất lượng mã nguồn kiểm thử và tính dễ bảo trì như "Test Smells" (Magic Numbers, Assertion Roulette, v.v.). | **GAP-M** | Hầu hết các nghiên cứu chỉ tập trung tối ưu coverage mà bỏ qua tính bảo trì mã nguồn test, ngoại trừ số ít như nghiên cứu của Ouédraogo (2026). | Trần Bích Trâm |
| **G6** | Thiếu báo cáo chi tiết về tỷ lệ biên dịch/chạy thành công (executability rate) và chi phí thực tế sử dụng API (số lượng token, chi phí tính bằng USD) của các LLM. | **GAP-M** | Chỉ có 2/44 bài báo cáo cụ thể tỷ lệ biên dịch/chạy được (Three-Stage Pipeline và Framework). Không bài nào báo cáo chi phí API. | Lê Trần Anh Khoa |
| **G7** | Thiếu đánh giá đối chứng chi tiết về hiệu năng của LLM trên các hàm có phụ thuộc ngoại vi phức tạp (Non-standalone/Mocking) so với các hàm độc lập (Standalone). | **GAP-D** | Các bộ dữ liệu lớn hiện nay chủ yếu đánh giá trên các hàm Standalone đơn giản; các framework như CANDOR, RefTest, ReAccept gặp khó khăn hoặc lảng tránh khi đối mặt với phụ thuộc lớp bên ngoài. | Trần Minh Quý, Trần Bích Trâm |
| **G8** | Thiếu các phân tích cụ thể về ảnh hưởng của độ phức tạp vòng (Cyclomatic Complexity) đến chất lượng unit test sinh bởi LLM (chưa xác định được ngưỡng độ phức tạp mà hiệu năng LLM bắt đầu giảm mạnh). | **GAP-D** | Các bài báo thường chỉ báo cáo kết quả trung bình trên toàn bộ dataset mà không phân nhóm theo Cyclomatic Complexity. | Vương Quốc Khánh |
| **G9** | Các bộ dữ liệu benchmark hiện tại bị phân mảnh và có nguy cơ rò rỉ dữ liệu cao (data contamination) vào tập huấn luyện của các LLM thương mại. | **GAP-D** | Các tập phổ biến như Defects4J, HumanEval, LeetCode, QuixBugs đều là nguồn mở công khai lâu năm trên GitHub. | Trương Đan Huy, Trần Minh Quý |
| **G10** | Tỷ lệ mã kiểm thử sinh bởi LLM bị lỗi biên dịch và lỗi cú pháp (ảo tưởng/hallucination) ở mức rất cao (có thể lên tới 86% trong các tác vụ phức tạp). | **GAP-S** | Thừa nhận chung trong phần hạn chế của các bài báo như Ye Shang (2025), Aminata Diop (2025), ReAccept (2025). | Trần Minh Quý, Trương Đan Huy, Lê Trần Anh Khoa |

---

### Phân loại các loại GAP:
* **GAP-T** (Technology): Công nghệ/tool nào chưa được thử cho task này?
* **GAP-M** (Metric): Khía cạnh nào chưa được đo?
* **GAP-D** (Dataset): Domain hoặc quy mô nào còn thiếu?
* **GAP-S** (Shared limitation): Hạn chế mà $\ge 40\%$ paper trong tập included cùng thừa nhận?
