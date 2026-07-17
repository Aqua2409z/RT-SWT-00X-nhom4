# Research Proposal: Evaluating GPT-4o-mini Zero-Shot Unit Test Generation on CLASSES2TEST with Mutation Score and EvoSuite Baseline
**Nhóm:** Team 5
**Thành viên:** Member 1 (MSSV) - PL; Member 2 (MSSV) - DG; Member 3 (MSSV) - LR; Member 4 (MSSV) - MS; Member 5 (MSSV) - RW
**Topic code:** TBD
**Ngày nộp:** 2026-06-16
**Version:** 1.0
**Trạng thái:** Đang chờ phê duyệt

> Lưu ý hành chính: Repo hiện không chứa danh sách tên thật của 5 thành viên, nên proposal này cố ý giữ 5 slot role cố định để nhóm chỉ cần thay tên/MSSV trước khi nộp.

## 2. Research Problem Statement

### 2.1 Background and Importance
Sinh unit test bằng LLM là bài toán có giá trị thực tiễn cao vì unit test vừa tốn thời gian vừa là tuyến phòng thủ sớm nhất chống regression trong quy trình phát triển phần mềm. Tuy nhiên, phần lớn các nghiên cứu vẫn đánh giá chất lượng test sinh ra chủ yếu qua coverage hoặc compile success, trong khi khả năng phát hiện lỗi mới là giá trị cốt lõi của test suite. ID 55 (MutGen, 2026) cho thấy một subject có thể đạt 100% line và branch coverage nhưng chỉ đạt 4% mutation score, nghĩa là coverage cao vẫn có thể gần như không phát hiện được lỗi.

### 2.2 State of the Art
AgoneTest (ID 9, ASE 2025) là nghiên cứu gần nhất với bài toán này ở mức class-level trên CLASSES2TEST và báo cáo đủ branch, line, method, mutation score, trong đó GPT-4o-mini zero-shot đạt 44.5% mutation score và 28.6% build success trên full set. AgoneTest Workshop (ID 12, ICSTW 2024) xác nhận cùng xu hướng trên cùng dataset, với GPT-4 zero-shot đạt 54.6% mutation coverage nhưng vẫn có khoảng hai phần ba test class bị reject ở compile hoặc execution stage. Test Wars (ID 21, ICST 2025) bổ sung góc nhìn so sánh LLM với EvoSuite, nhưng mutation chỉ là metric phụ và dataset là GitBug Java thay vì CLASSES2TEST. MutGen (ID 55, TSE 2026) là paper hiếm hoi đặt mutation score làm primary outcome và chứng minh mutation là chỉ số mạnh hơn coverage, nhưng dùng HumanEval-Java và LeetCode-Java là benchmark synthetic thay vì real-world Java repositories.

### 2.3 GAP
GAP chính của đề tài là **GAP-M (Metric)**. Trong 16 paper của evidence table, chỉ 4 paper báo cáo mutation score (ID 9, 12, 21, 55), nhưng không paper nào đồng thời thỏa mãn ba điều kiện: `(1)` mutation là primary outcome của RQ, `(2)` dataset là Java real-world, và `(3)` có baseline SBST để so sánh. ID 9 và ID 12 dùng CLASSES2TEST nhưng mutation chỉ là metric phụ và chỉ so với human-written tests; ID 21 có EvoSuite baseline nhưng mutation vẫn là metric phụ; ID 55 đặt mutation làm primary outcome nhưng dùng dataset synthetic. Vì vậy, khoảng trống nghiên cứu được chốt là: chưa có experiment nào đặt câu hỏi "GPT-4o-mini zero-shot đạt mutation score bao nhiêu trên CLASSES2TEST và đứng ở đâu so với EvoSuite?" như một research claim chính thức, có protocol đo lường và statistical decision được chốt trước khi chạy.

### 2.4 Motivation
Nếu khoảng trống này không được giải quyết, cộng đồng sẽ tiếp tục đánh giá LLM-generated tests bằng các chỉ số dễ đạt nhưng chưa đủ mạnh để phản ánh fault detection. Về thực tiễn, điều này có thể khiến nhóm phát triển tin rằng test do LLM sinh ra đã "tốt" chỉ vì compile được hoặc cover được nhiều code, trong khi thực tế test suite vẫn yếu trước lỗi tiềm ẩn.

## 3. Related Work

### 3.1 Overview

| Paper                          | Tool/LLM                                         | Dataset (size)                             | Metric                                                   | Best result                                                                        | Hạn chế chính                                                  |
| ------------------------------ | ------------------------------------------------ | ------------------------------------------ | -------------------------------------------------------- | ---------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| ID 9 - AgoneTest (2025)        | LLaMA 3.1-70B, GPT-4o-mini, Gemini-1.5-Pro       | CLASSES2TEST, 94 Java classes              | Branch, line, method, mutation, build success            | GPT-4o-mini zero-shot mutation 44.5%; best mutation 89.2% (LLaMA 3.1-70B few-shot) | Mutation chỉ tính trên green suite; không có EvoSuite baseline |
| ID 12 - AgoneTest WS (2024)    | GPT-4, GPT-3.5-turbo                             | CLASSES2TEST, 94 Java classes              | Branch, line, method, instruction, mutation              | GPT-4 zero-shot mutation 54.6%; human 69.1%                                        | Khoảng 66% test class bị reject; không so với EvoSuite         |
| ID 21 - Test Wars (2025)       | TestSpark-ChatGPT-4o, LLaMA-70B, EvoSuite, Kex   | GitBug Java, 136 bugs                      | Compile rate, line, branch, mutation, fault reproduction | EvoSuite best average mutation 30.56%; ChatGPT-4o compile 57.97%                   | Mutation là metric phụ; dataset khác CLASSES2TEST              |
| ID 55 - MutGen (2026)          | MutGen + LLaMA-3.3, GPT-4, DeepSeek-R1, EvoSuite | HumanEval-Java 104 + LeetCode-Java 100     | Mutation, line, branch                                   | MutGen mutation 89.5% / 89.1%; EvoSuite 69.5% / 58.9%                              | Dataset synthetic, không phải dự án Java thực tế               |
| ID 5 - HITS (2024)             | HITS + GPT-3.5-turbo, EvoSuite                   | 10 Java projects                           | Line, branch, pass rate                                  | Branch 48.12%, vượt EvoSuite 38.46%                                                | Không đo mutation                                              |
| ID 11 - JUnitGenie (2025)      | JUnitGenie + GPT-4o, EvoSuite                    | 2,258 focal methods từ 10 Java projects    | Branch, line, executable rate                            | Branch 56.86%, line 61.45%, vượt EvoSuite trung bình ~30 điểm phần trăm            | Không đo mutation                                              |
| ID 31 - ChatGPT vs SBST (2023) | GPT-3.5, EvoSuite                                | SF100, 248 Java classes                    | Statement coverage, bug detection                        | EvoSuite 67% stmt vs ChatGPT 50%; 55/212 bugs vs 44/212                            | Không đo mutation; query thủ công                              |
| ID 1 - ASTER (2025)            | GPT-4-turbo, Granite, Llama, EvoSuite, CodaMosa  | 4 Java SE + 4 Java EE + 283 Python modules | Line, branch, method, naturalness                        | Python line 78%, branch 77.2%; Java EE vượt EvoSuite 10.6%-26.4%                   | Không có benchmark chuẩn hóa dùng chung Java/Python            |

### 3.2 Pattern Analysis
- Nhìn chung, coverage là metric phổ biến nhất trong literature, còn mutation score vẫn hiếm và chỉ xuất hiện rõ ràng ở một số ít paper như ID 9, 12, 21 và 55. Điều này cho thấy cộng đồng đã có đồng thuận tương đối về cách đo "độ bao phủ", nhưng chưa có chuẩn mạnh tương tự cho fault-detection-oriented evaluation.
- Các paper dùng dataset rất phân mảnh: CLASSES2TEST, GitBug Java, SF100, Defects4J, HumanEval-Java, LeetCode-Java, Pynguin benchmarks. Vì vậy, các con số hiệu năng khó so sánh trực tiếp giữa nghiên cứu này với nghiên cứu khác.
- Khi LLM được so với baseline truyền thống, kết quả không nhất quán. Một số pipeline như HITS hay JUnitGenie vượt EvoSuite ở coverage, nhưng Test Wars và ChatGPT vs SBST lại cho thấy EvoSuite vẫn mạnh hơn về mutation hoặc bug detection. Điều này gợi ý rằng kết luận "LLM tốt hơn SBST" không thể đưa ra nếu không cố định dataset, metric và protocol.
- Nút thắt lặp lại ở các paper gần CLASSES2TEST là compile/build failure. ID 9 báo cáo GPT-4o-mini zero-shot chỉ đạt 28.6% build success; ID 12 cũng ghi nhận phần lớn test class bị reject. Vì vậy, proposal này phải đánh giá build success như một outcome độc lập, không được che khuất sau coverage.

### 3.3 GAP Mapping

| GAP loại | Evidence                                                                                                    | Status             |
| -------- | ----------------------------------------------------------------------------------------------------------- | ------------------ |
| GAP-M    | 4/16 paper có mutation; không paper nào đồng thời có mutation primary + real-world Java + EvoSuite baseline | Confirmed          |
| GAP-T    | Không paper nào benchmark nhiều LLM trên cả Java và Python với cùng protocol                                | Confirmed-Deferred |
| GAP-D    | Hơn 12 dataset khác nhau xuất hiện trong 16 paper; benchmark bị phân mảnh                                   | Confirmed-Deferred |
| GAP-S    | Đa số paper giới hạn một ngôn ngữ và nhiều paper thừa nhận compile/data leakage/generalizability limits     | Confirmed-Deferred |

## 4. Research Questions

> Toàn bộ RQ, metric, threshold và statistical decision dưới đây được chốt trước khi chạy experiment. RQ1 là primary confirmatory claim; RQ2-RQ4 là secondary pre-registered claims.

### RQ1 - Mutation Score as Primary Outcome
**RQ1:** Trên tập 30 focal classes Java được lấy từ CLASSES2TEST, GPT-4o-mini zero-shot (`gpt-4o-mini-2024-07-18`) có đạt **mutation score median >= 44.5%** hay không?

**Loại claim:** Absolute threshold  
**H0:** GPT-4o-mini zero-shot **không** đạt mutation score median >= 44.5% trên 30 focal classes.  
**H1:** GPT-4o-mini zero-shot **đạt** mutation score median > 44.5% trên 30 focal classes.  
**Metric:** Per-class mutation score (%) đo bằng PIT `pitest-maven`, nhóm toán tử `DEFAULTS`; nếu test suite không build hoặc không chạy được thì class đó nhận mutation score = `0.0` để giữ nguyên full-sample denominator.  
**Ngưỡng:** `44.5%` = **Case 2**, lấy từ ID 9 (ASE 2025, p.9 Table IV), là kết quả của GPT-4o-mini zero-shot trên cùng dataset CLASSES2TEST. Proposal này giữ cùng model nhưng dùng protocol nghiêm ngặt hơn vì không loại build-fail classes khỏi denominator.  
**Statistical test:** One-sample Wilcoxon signed-rank test, one-tailed, `alpha = 0.05`.

### RQ2 - Branch Coverage Floor on the Same Dataset
**RQ2:** Trên cùng 30 focal classes, GPT-4o-mini zero-shot có đạt **branch coverage median >= 41.9%** hay không?

**Loại claim:** Absolute threshold  
**H0:** GPT-4o-mini zero-shot **không** đạt branch coverage median >= 41.9%.  
**H1:** GPT-4o-mini zero-shot **đạt** branch coverage median > 41.9%.  
**Metric:** Per-class branch coverage (%) đo bằng JaCoCo Maven plugin `0.8.x`; class build-fail được gán `0.0` branch coverage để phản ánh end-to-end usefulness thay vì chỉ green-suite quality.  
**Ngưỡng:** `41.9%` = **Case 2**, lấy từ ID 9 (p.9 Table IV), là floor trực tiếp cho GPT-4o-mini zero-shot trên CLASSES2TEST.  
**Statistical test:** One-sample Wilcoxon signed-rank test, one-tailed, `alpha = 0.05`.

### RQ3 - Build Success as Practical Usability Constraint
**RQ3:** Trên cùng 30 focal classes, GPT-4o-mini zero-shot có đạt **build success rate >= 28.6%** hay không?

**Loại claim:** Absolute threshold  
**H0:** GPT-4o-mini zero-shot **không** đạt build success rate >= 28.6%.  
**H1:** GPT-4o-mini zero-shot **đạt** build success rate >= 28.6%.  
**Metric:** Build success rate (%) = số test classes compile được và chạy không crash trong Maven Surefire chia cho tổng 30 test classes đã sinh.  
**Ngưỡng:** `28.6%` = **Case 2**, lấy từ ID 9 (p.10 Table V), là full-set build success của GPT-4o-mini zero-shot trên CLASSES2TEST.  
**Statistical test:** Binomial exact test, one-tailed (`greater`), `p0 = 0.286`, `alpha = 0.05`.

### RQ4 - Comparative Position versus EvoSuite
**RQ4:** Mutation score phân phối theo class của GPT-4o-mini zero-shot có tốt hơn hoặc không thấp hơn đáng kể so với EvoSuite 1.2.0 trên cùng tập CLASSES2TEST hay không?

**Loại claim:** Comparative  
**H0:** GPT-4o-mini zero-shot **không** tốt hơn EvoSuite 1.2.0 về mutation score; về mặt thực dụng, median mutation của GPT thấp hơn EvoSuite quá 5 điểm phần trăm.  
**H1:** GPT-4o-mini zero-shot **không thấp hơn đáng kể** EvoSuite 1.2.0; median mutation của GPT ít nhất không thấp hơn EvoSuite quá 5 điểm phần trăm, và có thể vượt baseline.  
**Metric:** Per-class mutation score (%) cho GPT-4o-mini và EvoSuite trên cùng tập 30 classes; GPT build-fail classes vẫn nhận `0.0`.  
**Ngưỡng:** `Delta = -0.05` (không kém hơn quá 5 điểm phần trăm), là pre-registered practical margin vì literature chưa có EvoSuite benchmark trực tiếp trên CLASSES2TEST; margin được đặt bảo thủ hơn nhiều so với khoảng cách quan sát trong ID 21, nơi EvoSuite vượt rõ LLM trên GitBug Java.  
**Statistical test:** Mann-Whitney U test, one-tailed, `alpha = 0.05`; báo cáo thêm Vargha-Delaney `A12` effect size và chênh lệch median quan sát được.

## 5. Experiment Protocol

### 5.1 Pipeline tổng quan
1. Chọn 30 focal classes từ CLASSES2TEST bằng stratified random sampling: 3 classes mỗi repository, seed cố định `42`.
2. Kiểm tra mỗi class còn build được trong project gốc; nếu một class không thể reproduce build do dependency rot thì thay bằng class khác cùng repository và ghi log replacement.
3. Sinh một JUnit 5 test class cho mỗi focal class bằng GPT-4o-mini zero-shot với prompt và hyperparameters cố định trước.
4. Biên dịch và chạy test bằng Maven Surefire để xác định build success, lưu toàn bộ stdout/stderr.
5. Đo branch coverage và line coverage bằng JaCoCo trên từng class-level test output.
6. Đo mutation score bằng PIT trên cùng test suite; class nào không build được hoặc PIT không chạy được do lỗi test sẽ được ghi mutation = 0.0 theo protocol whole-sample.
7. Chạy EvoSuite 1.2.0 trên đúng 30 focal classes với search budget cố định để tạo baseline tự động.
8. Tổng hợp kết quả vào một bảng per-class và chạy statistical tests đã pre-register ở Section 4.

### 5.2 Dataset
**Tên dataset:** CLASSES2TEST  
**Nguồn:** Public benchmark được dùng trong ID 9 và ID 12. Published URL được nhúng trực tiếp trong cả hai paper là `https://anonymous.4open.science/r/classes2test`; upstream dataset gốc mà CLASSES2TEST mở rộng từ đó là `https://github.com/microsoft/methods2test`. DG sẽ pin mirror/commit khả dụng trong `data/raw/README.md` trước khi pilot.  
**Quy mô gốc:** 94 focal classes từ 10 Java repositories.  
**Quy mô nghiên cứu:** `N = 30` classes cho full experiment; `N_pilot = 6` classes cho pilot Tuần 7.  
**Domain:** Real-world Java class-level unit test generation.  
**Preprocessing:** Xác minh build script, package path, test dependency compatibility, và loại bỏ class nào không thể tái lập build trong môi trường nghiên cứu.  
**Sampling strategy:** Stratified random sampling, 3 classes/repository, seed `42`; nếu repository có class invalid do dependency issues, replacement được rút ngẫu nhiên từ cùng repository để giữ cân bằng domain.  
**Lý do chọn:** CLASSES2TEST là dataset gần GAP nhất vì vừa là benchmark real-world Java, vừa là nền của hai paper AgoneTest, cho phép so ngưỡng trực tiếp với GPT-4o-mini zero-shot đã có trong literature.

### 5.3 LLM/Tool Configuration
**Model:** `gpt-4o-mini-2024-07-18`  
**Hyperparameters:** `temperature = 0`, `top_p = 1`, `max_output_tokens = 2048`, `frequency_penalty = 0`, `presence_penalty = 0`  
**Prompting strategy:** Zero-shot  
**Prompt template:**

```text
You are an expert Java test engineer.

Task: Generate exactly one compilable JUnit 5 test class for the focal Java class below.

Requirements:
1. Use only Java and JUnit 5 code.
2. Reuse the focal class package when appropriate.
3. Import only dependencies that are already available in the target project.
4. Do not use placeholders, pseudocode, or explanatory text.
5. If object construction is impossible from visible APIs, create the smallest valid test you can without inventing unavailable methods.
6. Return only the final Java test class code.

Repository: {repository_name}
Focal class path: {class_path}
Focal class source:
{class_source}
```

**Lý do cấu hình:** Zero-shot và temperature = 0 được giữ để bám sát cấu hình reference của ID 9 trên cùng model/dataset. Proposal cố ý không dùng few-shot để tránh pha trộn hiệu ứng prompt engineering với đóng góp chính về evaluation protocol.

### 5.4 Measurement

| Metric             | Tool + version                                         | Đơn vị                         | Ground truth source                                 | IAA |
| ------------------ | ------------------------------------------------------ | ------------------------------ | --------------------------------------------------- | --- |
| Mutation score     | PIT `pitest-maven` `1.15.x`, operator group `DEFAULTS` | % per class                    | PIT report trên project bytecode và generated tests | N/A |
| Branch coverage    | JaCoCo Maven plugin `0.8.x`                            | % per class                    | JaCoCo XML/CSV reports                              | N/A |
| Line coverage      | JaCoCo Maven plugin `0.8.x`                            | % per class                    | JaCoCo XML/CSV reports                              | N/A |
| Build success rate | Maven Surefire + project compiler plugin               | pass/fail per class, % overall | Maven build and test execution logs                 | N/A |

**Measurement rule quan trọng:** Với RQ1 và RQ2, class build-fail được gán score `0.0` thay vì bị loại khỏi denominator. Quy tắc này được chốt trước khi chạy để tránh green-suite bias và bảo toàn end-to-end meaning của LLM-generated test usefulness.

### 5.5 Baseline
**Tên baseline:** EvoSuite `1.2.0`  
**Cấu hình:** Search budget `60s/class`; criterion `branch`; chạy trên đúng 30 focal classes dùng cùng project checkout như LLM arm.  
**Nguồn:** Comparative precedent từ ID 21 và ID 31; proposal này chuyển baseline sang CLASSES2TEST để lấp đầy góc C của GAP-M.  
**Output để reproduce:** Một test suite/class, cùng pipeline JaCoCo + PIT + Surefire như LLM arm.

### 5.6 Statistical Analysis Plan
**RQ1:** One-sample Wilcoxon signed-rank, one-tailed, `mu0 = 0.445`, `alpha = 0.05`  
**RQ2:** One-sample Wilcoxon signed-rank, one-tailed, `mu0 = 0.419`, `alpha = 0.05`  
**RQ3:** Exact binomial test, one-tailed, `p0 = 0.286`, `alpha = 0.05`  
**RQ4:** Mann-Whitney U, one-tailed, `alpha = 0.05`  

**Lý do chọn test:**
- RQ1 và RQ2 dùng điểm số liên tục dạng tỷ lệ theo class, không có giả định phân phối chuẩn an toàn với `N = 30`, nên dùng Wilcoxon.
- RQ3 là outcome nhị phân build-success/build-fail, nên dùng binomial exact test thay vì xấp xỉ chuẩn.
- RQ4 là so sánh hai phân phối score giữa hai hệ thống, nên dùng Mann-Whitney U theo draft statistical plan từ RBL-2; đồng thời báo cáo thêm `A12` để tránh chỉ dựa vào p-value.

**Effect size plan:**
- RQ1, RQ2: rank-biserial effect size.  
- RQ3: observed proportion difference và Wilson confidence interval.  
- RQ4: Vargha-Delaney `A12` và chênh lệch median quan sát.

**N và power safeguard:**
- Full run pre-register `N = 30` classes; pilot run pre-register `N = 6` classes.  
- Nếu pilot cho thấy số class analyzable cho RQ1/RQ2 thấp hơn `24`, nhóm sẽ tăng full sample lên `40` classes trước khi chạy full experiment để giữ practical power cho medium effects ở mức chấp nhận được.  
- Quy tắc mở rộng sample này được chốt trước, chỉ phụ thuộc vào technical analyzability chứ không phụ thuộc vào kết quả metric, nên không tạo HARKing.

## 6. Evaluation Plan

### 6.1 Bảng tiêu chí đánh giá

| RQ  | Metric                                      | Ngưỡng                                    | Test                   | H0 bị reject khi...                                                   | Kết quả âm tính có ý nghĩa?                                                                                                                                       |
| --- | ------------------------------------------- | ----------------------------------------- | ---------------------- | --------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| RQ1 | Median mutation score (%)                   | `>= 44.5%`                                | Wilcoxon 1 mẫu         | `p < 0.05` và observed median `> 0.445`                               | Có. Nếu fail, đó là bằng chứng rằng whole-sample mutation của GPT-4o-mini không tái lập được mức literature trên cùng benchmark khi không loại build-fail classes |
| RQ2 | Median branch coverage (%)                  | `>= 41.9%`                                | Wilcoxon 1 mẫu         | `p < 0.05` và observed median `> 0.419`                               | Có. Nếu fail, coverage floor trên CLASSES2TEST không ổn định dưới protocol nghiêm ngặt hơn                                                                        |
| RQ3 | Build success rate (%)                      | `>= 28.6%`                                | Binomial exact         | `p < 0.05` và observed proportion `>= 0.286`                          | Có. Nếu fail, practical deployability của zero-shot pipeline là vấn đề lớn hơn quality-on-success                                                                 |
| RQ4 | Per-class mutation distribution vs EvoSuite | `Delta >= -0.05` theo diễn giải thực dụng | Mann-Whitney U + `A12` | `p < 0.05` và median GPT không thấp hơn EvoSuite quá 5 điểm phần trăm | Có. Nếu fail, EvoSuite vẫn là baseline mạnh hơn cho real-world Java trên CLASSES2TEST                                                                             |

### 6.2 Diễn giải tổ hợp kết quả
- **Double positive mạnh (RQ1+, RQ3+, RQ4+):** GPT-4o-mini zero-shot vừa đạt literature floor vừa giữ practical executability và có vị trí cạnh tranh với EvoSuite. Đây là bằng chứng mạnh nhất rằng LLM zero-shot có thể là baseline thực nghiệm nghiêm túc trên CLASSES2TEST.
- **Mixed result 1 (RQ3+ nhưng RQ1-/RQ4-):** Test sinh ra compile được ở mức chấp nhận được nhưng fault-detection quality vẫn yếu. Kết luận khi đó là "usability có, effectiveness chưa đủ", và nghiên cứu vẫn đóng góp benchmark âm có giá trị.
- **Mixed result 2 (RQ1+ hoặc RQ2+ nhưng RQ3-):** Một số class xanh có chất lượng tốt, nhưng pipeline end-to-end chưa đủ ổn định để dùng thực tế. Kết luận là compile fragility mới là bottleneck chính, không phải coverage/mutation khi test đã chạy được.
- **Double negative (RQ1-, RQ3-, RQ4-):** Zero-shot GPT-4o-mini không đạt cả literature floor lẫn baseline SBST trong điều kiện real-world Java class-level ATG. Đây vẫn là kết quả có giá trị vì cung cấp benchmark chuẩn hóa mà literature hiện thiếu.

### 6.3 Sub-group analysis
Proposal pre-register hai phân tích subgroup mang tính exploratory, không dùng để thay đổi RQ chính:
- **Theo repository:** Chỉ chạy khi mỗi repository còn ít nhất `n_group >= 3` classes sau preprocessing; báo cáo median metric theo repo để xem hiện tượng có bị chi phối bởi một codebase cụ thể hay không.
- **Theo class complexity proxy:** Nếu metadata về LOC hoặc cyclomatic complexity lấy được từ dataset, chia `low` và `high` theo median split; chỉ chạy khi mỗi nhóm có `n_group >= 10`.

## 7. Threats to Validity

### 7.1 Internal Validity
**Threat:** Cloud LLM có thể bị silent-update hoặc thay đổi hành vi theo thời gian.  
**Mitigation:** Pin model version ở mức `gpt-4o-mini-2024-07-18`, log timestamp, request configuration và raw response cho từng class; không đổi model sau khi proposal được duyệt.

**Threat:** API failure, retry noise, hoặc rate-limit có thể làm một số class thất bại vì hạ tầng thay vì vì model.  
**Mitigation:** Chỉ retry cho lỗi transport hoặc rate-limit, không retry khi model đã trả nội dung; mọi retry được log riêng để LR và MS kiểm tra.

**Threat:** Baseline EvoSuite có thể nhạy với configuration.  
**Mitigation:** Cố định version `1.2.0`, search budget `60s/class`, criterion `branch`, và dùng cùng project checkout cho cả hai arms.

### 7.2 External Validity
**Threat:** Nghiên cứu chỉ dùng Java và chỉ trên CLASSES2TEST, nên khó khái quát trực tiếp sang Python, C#, hoặc method-level generation.  
**Mitigation:** Đóng khung claim ở mức "real-world Java class-level unit test generation" và không suy rộng ra đa ngôn ngữ.

**Threat:** Full sample chỉ gồm 30 classes thay vì toàn bộ 94 classes.  
**Mitigation:** Dùng stratified sampling theo repository và ghi rõ seed, danh sách class được chọn, cùng quy tắc replacement để người khác có thể tái lập hoặc mở rộng.

### 7.3 Construct Validity
**Threat:** Mutation score mạnh hơn coverage nhưng vẫn không phản ánh đầy đủ maintainability, readability, hoặc oracle quality.  
**Mitigation:** Báo cáo đồng thời mutation, branch coverage và build success; giữ mutation là primary metric nhưng không dùng nó như chỉ số duy nhất.

**Threat:** Gán `0.0` cho build-fail classes có thể làm metric nghiêm ngặt hơn literature cũ.  
**Mitigation:** Pre-register quy tắc này trước khi chạy, giải thích rõ đây là deliberate whole-sample protocol để loại green-suite bias, và báo cáo song song số build-fail thô để người đọc diễn giải đúng.

### 7.4 Conclusion Validity
**Threat:** Với build success thấp, số quan sát hiệu lực cho mutation/coverage có thể không đủ power.  
**Mitigation:** Chốt rule mở rộng sample từ 30 lên 40 classes nếu pilot cho thấy dưới 24 class analyzable cho RQ1/RQ2.

**Threat:** Có nhiều RQ phụ nên người đọc có thể over-interpret p-value.  
**Mitigation:** Xác định RQ1 là primary confirmatory claim; RQ2-RQ4 là secondary pre-registered claims; luôn báo cáo effect size và observed median/proportion thay vì chỉ nêu p-value.

## 8. Timeline & Resources

### 8.0 Phân công vai trò

| Role | Thành viên               | Trách nhiệm trong experiment                                                           |
| ---- | ------------------------ | -------------------------------------------------------------------------------------- |
| PL   | Member 1 (điền tên/MSSV) | Điều phối tiến độ, khóa scope, kiểm tra nhất quán §2-§8, nộp proposal và amendment     |
| DG   | Member 2 (điền tên/MSSV) | Stage dataset, kiểm tra buildability của classes, chuẩn bị data README và sampling log |
| LR   | Member 3 (điền tên/MSSV) | Cấu hình API, viết script gọi LLM, lưu request/response log và cost log                |
| MS   | Member 4 (điền tên/MSSV) | Viết metric/stat scripts, chạy JaCoCo/PIT/stat tests, xác minh output của LR           |
| RW   | Member 5 (điền tên/MSSV) | Viết/mài proposal, threats, figures, formatting cuối cùng và slide support             |

### 8.1 Resource Inventory

| Tài nguyên                                    | Trạng thái | Owner   | Ghi chú                                                                                                                                                                                                                            |
| --------------------------------------------- | ---------- | ------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CLASSES2TEST dataset                          | ⚠️          | DG      | Published URL đã pin từ paper: `https://anonymous.4open.science/r/classes2test`; upstream parent dataset: `https://github.com/microsoft/methods2test`; vẫn cần mirror tối thiểu trước pilot vì anonymous link có thể không ổn định |
| OpenAI API key                                | ⚠️          | LR      | Chưa thấy key trong repo; cần tạo project riêng và giới hạn ngân sách trước full run                                                                                                                                               |
| Java toolchain (Maven, JaCoCo, PIT, EvoSuite) | ✅          | LR + MS | Tất cả là công cụ public/open-source; cần freeze version trong README                                                                                                                                                              |
| Compute                                       | ✅          | LR      | Laptop JVM hoặc Colab đều đủ cho sample 30-40 classes                                                                                                                                                                              |
| Ground truth / manual annotation              | ✅          | DG      | Không cần human annotation; metric đều được instrument tự động                                                                                                                                                                     |

### 8.2 Chi phí ước tính

| Item                          | Số lượng      | Đơn giá  | Tổng         |
| ----------------------------- | ------------- | -------- | ------------ |
| OpenAI API project budget cap | 1 project cap | USD 5.00 | USD 5.00     |
| EvoSuite                      | 1 tool        | USD 0.00 | USD 0.00     |
| JaCoCo                        | 1 tool        | USD 0.00 | USD 0.00     |
| PIT                           | 1 tool        | USD 0.00 | USD 0.00     |
| Local storage/logging         | 1 workspace   | USD 0.00 | USD 0.00     |
| **Tổng ngân sách kế hoạch**   |               |          | **USD 5.00** |

> Ghi chú: Vì giá của model variant có thể thay đổi theo thời điểm provider billing, proposal khóa **budget cap** thay vì khóa một con số token-cost cố định. LR phải chụp dashboard budget setting trước khi chạy full experiment.

### 8.3 Timeline chi tiết

| Tuần | Hoạt động                                            | Owner        | Checkpoint - output cụ thể                                    |
| ---- | ---------------------------------------------------- | ------------ | ------------------------------------------------------------- |
| 5    | Viết nháp proposal §2-§7                             | PL + RW + DG | `team-synthesis/proposal.md` draft                            |
| 5    | Stage dataset, kiểm tra 10 repos, chốt sampling seed | DG           | `data/raw/README.md` + danh sách 30 class candidates          |
| 5    | Setup OpenAI project và test 1 sample call           | LR           | `scripts/test_api.*` + request log                            |
| 5    | Draft metric pipeline JaCoCo/PIT                     | MS           | `scripts/compute_metrics.*` draft                             |
| 6    | Review chéo §4-§6 và hoàn thiện §8                   | PL + MS + RW | Proposal v1.0 ready for advisor review                        |
| 6    | Nộp proposal defense                                 | PL           | `team-synthesis/proposal.md` v1.0                             |
| 7    | Chạy pilot trên 6 classes                            | LR + DG + MS | `results/pilot_*.csv` + pilot note                            |
| 7    | Họp pilot và quyết định có amendment hay không       | PL           | `proposal-amendment-v1.1.md` nếu cần                          |
| 8    | Chạy full experiment trên 30 hoặc 40 classes         | LR + MS      | `results/full_llm_output.csv`, `results/full_metrics.csv`     |
| 8    | Sinh EvoSuite baseline và chạy comparative analysis  | LR + MS      | `results/full_evosuite_output.csv`, `results/full_analysis.*` |
| 9-10 | Viết paper/report và chuẩn bị presentation           | Tất cả       | figures, result tables, defense materials                     |

### 8.4 Contingency Plan
- **Nếu dataset mirror không ổn định trước pilot:** DG phải khóa một local mirror tối thiểu của đúng 30 class candidates, không phụ thuộc download lại trong tuần chạy.
- **Nếu API rate limit hoặc budget alert xảy ra:** LR chia batch theo repository và chạy ngoài giờ; không đổi model nếu chưa có amendment được duyệt.
- **Nếu pilot cho thấy dưới 24 class analyzable cho RQ1/RQ2:** Mở rộng sample lên 40 classes theo rule đã pre-register ở §5.6.
- **Nếu proposal chưa được duyệt cuối Tuần 6:** Giữ nguyên RQ1-RQ3, tạm hoãn RQ4 comparative analysis sang exploratory appendix để không chậm pilot.

### 8.5 Checkpoint per member

| Role | Tuần 5                         | Tuần 6                  | Tuần 7                | Tuần 8              | Tuần 9-10                |
| ---- | ------------------------------ | ----------------------- | --------------------- | ------------------- | ------------------------ |
| PL   | Review logic §2-§7             | Submit proposal         | Pilot meeting note    | Consistency audit   | Final integrate          |
| DG   | Dataset README + sampling plan | Confirm resource status | Pilot class manifest  | Full class manifest | Data appendix            |
| LR   | API smoke test                 | Budget cap screenshot   | Pilot generation log  | Full generation log | Reproduction note        |
| MS   | Metric script draft            | Freeze stat plan        | Pilot analysis file   | Full analysis file  | Results table            |
| RW   | Draft §2, §7, formatting       | Proofread v1.0          | Pilot figure template | Final figures       | Paper/presentation draft |

### 8.6 Amendment Rule
Amendment chỉ được nộp vì lý do kỹ thuật phát hiện từ pilot, không phải vì kết quả tốt/xấu. Các tình huống hợp lệ gồm:
- sample analyzability thấp hơn rule đã dự kiến,
- lỗi metric implementation hoặc incompatibility của toolchain,
- dataset replacement cần thiết do class không còn build được.

Các thay đổi **không hợp lệ** gồm:
- đổi threshold vì thấy kết quả thấp,
- thêm metric mới vì thấy "thú vị",
- đổi RQ chính sau khi đã có pilot metrics.
