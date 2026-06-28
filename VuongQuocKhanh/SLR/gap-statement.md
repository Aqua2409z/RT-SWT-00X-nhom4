# Gap Statement – LLM for Unit Test Case Generation

**Evidence table:** N = 34 papers

---

## GAP-T (Technology)

### Mô tả

Thiếu nghiên cứu so sánh trực tiếp GPT-4 với cả human-written tests và traditional automated test generation tools dưới cùng điều kiện thực nghiệm.

### Bằng chứng

**Nguồn bằng chứng:** Cột **Tool/LLM**

Các paper sử dụng:

* CODAMOSA → Codex
* HITS → GPT-3.5-turbo
* TESTEVAL → GPT-4 và các LLM khác
* Electronics 2025 → Llama-2-7B
* Enhancing LLMs for Text-to-Testcase Generation → Fine-tuned GPT-3.5

Phần lớn nghiên cứu:

* chỉ đánh giá một LLM riêng lẻ; hoặc
* chỉ so sánh với một baseline duy nhất.

Không nhiều nghiên cứu thực hiện so sánh đồng thời:

```text
GPT-4
vs Human-written Tests
vs Randoop-generated Tests
```

### Kết luận GAP

Chưa có nhiều bằng chứng thực nghiệm toàn diện về hiệu quả của GPT-4 so với cả con người và công cụ sinh test truyền thống.

---

## GAP-M (Metric)

### Mô tả

Thiếu nghiên cứu đánh giá đồng thời Branch Coverage và Mutation Score.

### Bằng chứng

**Nguồn bằng chứng:** Cột **Metric**

Các metric xuất hiện trong evidence table:

* Line Coverage
* Branch Coverage
* Pass Rate
* Accuracy
* Precision
* Recall
* F1-score
* Mutation Score

Nhiều paper chỉ đo Coverage.

Một số paper chỉ đo Mutation Score.

Rất ít nghiên cứu đánh giá đồng thời:

```text
Branch Coverage
+
Mutation Score
```

để phản ánh cả:

* độ bao phủ mã nguồn;
* khả năng phát hiện lỗi của test case.

### Kết luận GAP

Chưa có khung đánh giá thống nhất phản ánh đồng thời coverage và fault-detection capability.

---

## GAP-D (Dataset)

### Mô tả

Thiếu phân tích ảnh hưởng của Cyclomatic Complexity đến chất lượng unit test được sinh bởi LLM.

### Bằng chứng

**Nguồn bằng chứng:** Cột **Dataset**

Các dataset xuất hiện:

* HumanEval
* Defects4J
* TESTEVAL
* Methods2Test
* Java Open-source Projects
* Python Open-source Projects

Các nghiên cứu chủ yếu báo cáo:

* Coverage trung bình;
* Mutation Score trung bình;
* Pass Rate trung bình.

Không nhiều nghiên cứu phân tích:

```text
Cyclomatic Complexity
```

và mối quan hệ của nó với:

* Branch Coverage;
* Mutation Score;
* Test Quality.

### Kết luận GAP

Chưa xác định rõ ngưỡng độ phức tạp mà hiệu năng sinh unit test của LLM bắt đầu suy giảm.

---

## Phát biểu GAP tổng hợp

Mặc dù nhiều nghiên cứu đã áp dụng LLM cho bài toán sinh unit test tự động, 
vẫn còn thiếu các đánh giá thực nghiệm toàn diện giữa GPT-4, 
human-written tests và traditional automated testing tools dưới cùng điều kiện thực nghiệm.
 Đồng thời, các nghiên cứu hiện tại chưa đánh giá nhất quán cả Branch Coverage và Mutation Score, 
cũng như chưa phân tích đầy đủ tác động của Cyclomatic Complexity đến chất lượng unit test được sinh bởi LLM. 
Những khoảng trống này tạo động lực cho nghiên cứu nhằm đánh giá toàn diện hiệu quả của GPT-4 trong unit test generation.
