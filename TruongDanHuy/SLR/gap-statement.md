# Gap Statement - LLM cho sinh unit test

Evidence table: N = 16 (V2 Include, đã có PDF; số liệu trích từ full-text)  
V2 Exclude: N = 33 (bao gồm 28 paywalled EC-A)

---

## Các khoảng trống phát hiện

### GAP-T (Technology): Thiếu đánh giá đa mô hình chuẩn hóa trên nhiều ngôn ngữ

**Bằng chứng từ cột Công cụ/LLM trong evidence table:**

- **[ID 1 – ASTER, p.7–8, Fig.5–6]** Chỉ so sánh các model trong cùng một pipeline trên tập Java và tập Python riêng biệt (Fig.5: Java SE vs EvoSuite; Fig.6: Python vs CodaMosa). Không có benchmark chuẩn hóa dùng chung. **[Finding 3, p.8]**: Smaller models (Granite-34b) chỉ thua GPT-4 có 0.1%/6.3%/2.7% về line/branch/method — không có ngưỡng hiệu suất chuẩn.
- **[ID 3 – ShreyaBhatia, p.4–5, Table 1–2]** ChatGPT (GPT-3.5) vs Pynguin trên 60 Python projects; không có Java. Large (100–300 LOC): ChatGPT avg stmt **77.43%** / branch **73.39%**; Pynguin **81.63%** / **78.36%** (EvoSuite-based tool tốt hơn).
- **[ID 13 – LinYang, p.7]** So sánh DC-33B vs DC-7B: line +**12.75%** / branch +**23.10%**. NTD: GPT-4 có 65/835 defects testable, nhưng chỉ trên Defects4J Java (rủi ro data leakage).
- **[ID 21 – Test Wars, p.5, Fig.3]** TestSpark-ChatGPT-4o: compile **57.97%** / line **38.13%** / branch **24.63%**; chỉ trên GitBug Java; TestSpark-LLaMA-70B gần như fail hoàn toàn (1229/1360 runs fail vì context window 4–8k).
- **Kết quả khảo sát:** Không có paper nào trong 16 bài Include thực hiện benchmark chuẩn hóa so sánh nhiều mô hình (ít nhất GPT-4 + 1 LLM khác) đồng thời trên cả Java lẫn Python với cùng một protocol đo lường.

### GAP-M (Metric): Không có paper nào đồng thời có mutation là primary outcome, dataset real-world, và baseline EvoSuite

**Bằng chứng nền:** **ID 55** (MutGen, TSE 2026) **[p.3, Section II “Motivation”]** chứng minh: subject id_81 đạt 100% line/branch coverage nhưng chỉ **4% mutation score** với vanilla LLM (GENvanilla) — lý do cần phải đo mutation, không chỉ coverage. **[p.9, Table I]** GENvanilla: mutation score chỉ **77.9%** (HumanEval) trong khi line/branch coverage lên đến **96.2%/92.8%**.

**Bảng 3 góc — mỗi paper chỉ đạt 2/3 điều kiện:**

| Paper | (A) Mutation primary? | (B) Real-world Java? | (C) vs EvoSuite? | Còn thiếu |
|-------|-----------------------|----------------------|-----------------|------------|
| **[ID 9]** AgoneTest (ASE 2025) | phụ, green-suite |  CLASSES2TEST |  chỉ vs human | Góc A + C |
| **[ID 12]** AgoneTest Workshop (ICSTW 2024) |  phụ, green-suite |  CLASSES2TEST |  chỉ vs human | Góc A + C |
| **[ID 21]** Test Wars (ICST 2025) |  phụ |  GitBug Java |  EvoSuite 1.2.0 | Góc A |
| **[ID 55]** MutGen (TSE 2026) |  primary |  HumanEval+LeetCode |  EvoSuite | Góc B |
| **Experiment (kế hoạch)** |  H0/H1 chính thức |  CLASSES2TEST |  EvoSuite 1.2.0 | — |

- **[ID 9, p.9, Table IV “AVERAGE OF METRICS”]** gpt-4o-mini zero-shot: mutation **44.5%** — là metric phụ. **[p.10, Table V]** Build success zero-shot: **28.6%**. **[p.9, Sec.VI-B]** Pass (green suite): *“between 30% and 38%”* — tuyên bố rõ *“mutation score chỉ tính trên green suite”*.
- **[ID 12, p.9, Table V “METRICS COMPUTED FOR EACH MODEL”]** gpt-4 zero-shot: mutation **0.546** (54.6%); human: **0.691** (best). **[p.10, Sec.C]**: ~66% test classes bị reject do compile fail.
- **[ID 21, p.9, Venn diagram]** Ghi nhận: *“even smaller intersection with both tools in the mutation scores”* — mutation là phụ.
- **[ID 55, p.9, Table I]** MUTGEN: HumanEval-Java **89.5%** / LeetCode **89.1%** mutation score. EvoSuite: **69.5%** / **58.9%**. Mutation là RQ chính — nhưng dataset synthetic.
- **Khoảng trống:** Chưa có experiment nào đặt câu hỏi *"GPT-4o-mini zero-shot đạt mutation score bao nhiêu trên CLASSES2TEST so với EvoSuite?"* làm H0/H1 chính thức với giao thức đo tường minh.

### GAP-D (Dataset): Thiếu benchmark dùng chung và rủi ro data leakage

**Bằng chứng từ cột Bộ dữ liệu trong evidence table:**

- **[ID 12, ID 9 – AgoneTest]** Dùng CLASSES2TEST (mở rộng METHODS2TEST): 10 repos Java, 94 focal classes.
- **[ID 13 – LinYang]** Dùng Defects4J 2.0 (17 Java projects) — **có rủi ro data leakage** vì Defects4J là public và có thể nằm trong training data LLMs.
- **[ID 21 – Test Wars, p.5]** Dùng GitBug Java (LLM-untainted) + SF110 để tránh data leakage — nhưng là benchmark riêng, ít paper khác dùng.
- **[ID 41 – CodaMosa]** 486 Python benchmark subjects từ Pynguin; **Python only**.
- **[ID 16 – JsonATG]** fastjson2 Java library — domain rất hẹp, không generalizable.
- **[ID 55 – MutGen]** HumanEval-Java + LeetCode-Java — synthetic problems, không phải real-world projects.
- **Khoảng trống:** Các bài dùng các benchmark khác nhau (CLASSES2TEST, Defects4J, GitBug, HumanEval-Java, SF100, QuixBugs…); không có benchmark chuẩn hóa chung được dùng qua nhiều paper để so sánh trực tiếp. Thêm vào đó, rủi ro data leakage (Defects4J, HumanEval) chưa được giải quyết nhất quán.

## Phát biểu GAP tổng hợp

Dựa trên 16 bài Include (số liệu trích từ full-text PDF), ba khoảng trống chính hiện nay là:

**(1) GAP-T:** Không paper nào thực hiện so sánh đa mô hình chuẩn hóa (ít nhất 2 LLM) đồng thời trên Java và Python với cùng một protocol — ID 1 (ASTER) gần nhất nhưng chỉ trong một pipeline.

**(2) GAP-M:** ID 55 (MutGen) chứng minh coverage ≠ fault-detection. Trong 4 paper có mutation, không paper nào cùng lúc có cả ba: **mutation là primary outcome + dataset Java real-world + so sánh vs EvoSuite**. ID 9 (CLASSES2TEST, nhưng không EvoSuite, mutation phụ); ID 21 (EvoSuite, nhưng mutation phụ); ID 55 (mutation primary, nhưng dataset synthetic). Đây là **intersection gap** chưa được lấp đầy.

**(3) GAP-D:** Benchmark phân mảnh và rủi ro data leakage; không có dataset chuẩn dùng chung cross-paper cho cả Java và Python.

Gap này dẫn trực tiếp đến RQ trong `experiment/01_rq.md`: đặt mutation score làm H0/H1 chính thức khi đánh giá GPT-4o-mini zero-shot trên CLASSES2TEST với EvoSuite làm baseline — cùng lúc lấp đầy cả 3 góc mà không paper nào hiện tại đã làm.
