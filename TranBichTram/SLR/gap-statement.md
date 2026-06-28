# Gap Statement – LLM for Unit Test Case Generation

**Evidence table: N = 8 papers**

## Các khoảng trống phát hiện

### GAP-T (Technology): Thiếu sự tích hợp sâu giữa phân tích chương trình và LLM để giảm thiểu ảo giác (hallucination)
**Bằng chứng:** [Cột Tool/LLM – TestPilot, TestCTRL, TELPA, CANDOR đều dùng LLM/Multi-agent nhưng vẫn phụ thuộc vào thử sai hoặc chưa giải quyết triệt để vấn đề phụ thuộc giả].
Các công cụ hiện tại như TestPilot hay TestCTRL chủ yếu phụ thuộc vào cơ chế phản hồi động hoặc thử sai (trial-and-error).
Dù TELPA có kết hợp phân tích tĩnh, công cụ này vẫn gặp hạn chế khi xử lý cấu trúc đệ quy và vòng lặp.
CANDOR sử dụng khung đa tác nhân (multi-agent) nhưng chưa giải quyết triệt để vấn đề sinh ra các phụ thuộc giả (fabricated dependencies).
### GAP-M (Metric): Thiếu chỉ số đo lường khả năng bảo trì mã nguồn test (Maintainability)
**Bằng chứng:** [Cột Metric – Benchmark, TestCTRL, TELPA tập trung vào coverage; chưa paper nào tối ưu hóa theo Test Smells].
Benchmark, TestCTRL, TELPA chủ yếu tập trung vào độ bao phủ (Coverage) làm metric đánh giá chính.
Phần lớn các nghiên cứu chưa đưa các chỉ số như "Test Smells" vào quy trình tối ưu hóa khi sinh test, ngoại trừ Ouédraogo et al. có đề cập đến vấn đề này.

### GAP-D (Dataset): Sự phân mảnh giữa các tập dữ liệu và tính đa dạng ngôn ngữ
**Bằng chứng:** [Cột Dataset – Các tập dữ liệu hiện bị giới hạn theo domain ngôn ngữ: Java (CANDOR, RefTest, ReAccept) hoặc Python (Benchmark, TELPA) và thiếu đánh giá trên quy mô lớn đa ngôn ngữ].
Các nghiên cứu hiện tại bị chia cắt cục bộ theo ngôn ngữ lập trình, ví dụ như Java (CANDOR, RefTest, ReAccept) hoặc Python (Benchmark, TELPA).
Thiếu các đánh giá quy mô lớn (large-scale) trên đa ngôn ngữ với một bộ metric chuẩn hóa để kiểm chứng tính tổng quát của kỹ thuật prompting.

## Phát biểu GAP tổng hợp
Hiện nay, các kỹ thuật tạo unit test bằng LLM vẫn phụ thuộc quá mức vào cơ chế thử sai và thực thi động, dẫn đến tỷ lệ lỗi biên dịch cao lên tới 86% và thiếu khả năng bảo trì mã nguồn test trong dài hạn. Cần một phương pháp lai (hybrid approach) tích hợp sâu phân tích chương trình và các metric về tính bảo trì để chuyển dịch từ việc chỉ tạo ra các bộ test "chạy được" sang các bộ test "chất lượng cao và bền vững".