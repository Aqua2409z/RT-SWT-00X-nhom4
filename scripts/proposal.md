# Research Proposal: Sampled Replication of AgoneTest 2025 on CLASSES2TEST with GPT-4o-mini Zero-Shot and EvoSuite Baseline
**Nhóm:** Team 5  
**Thành viên:** Member 1 (MSSV) - PL; Member 2 (MSSV) - DG; Member 3 (MSSV) - LR; Member 4 (MSSV) - MS; Member 5 (MSSV) - RW  
**Topic code:** TBD  
**Ngày cập nhật:** 2026-06-29  
**Version:** 1.1  
**Trạng thái:** Amendment after paper-artifact audit

> Lưu ý hành chính: Repo hiện không chứa danh sách tên thật của 5 thành viên, nên proposal này cố ý giữ 5 slot role cố định để nhóm chỉ cần thay tên/MSSV trước khi nộp.

## 2. Research Problem Statement

### 2.1 Background and Importance
Sinh unit test bằng LLM có giá trị thực tiễn cao vì unit test vừa tốn thời gian vừa là tuyến phòng thủ sớm nhất chống regression. Tuy nhiên, phần lớn literature vẫn đánh giá chất lượng test sinh ra chủ yếu qua coverage hoặc compile success, trong khi khả năng phát hiện lỗi mới là giá trị cốt lõi của test suite. ID 55 (MutGen, 2026) cho thấy một subject có thể đạt 100% line và branch coverage nhưng chỉ đạt 4% mutation score, nghĩa là coverage cao vẫn có thể gần như không phát hiện được lỗi.

### 2.2 State of the Art
AgoneTest (ID 9, ASE 2025) là nghiên cứu gần nhất với bài toán này ở mức class-level trên CLASSES2TEST và báo cáo đủ branch, line, method, mutation score, build success, và test smells. Quan trọng hơn, paper này dùng **full class-level CLASSES2TEST universe** ở mức `147,473` class-level instances từ `9,410` repositories cho build-success reporting, còn các giá trị `41.9 / 64.8 / 77.2 / 44.5` của GPT-4o-mini zero-shot là **compiled-only averages**. AgoneTest Workshop (ID 12, ICSTW 2024) dùng một **subset nhỏ hơn** gồm `94 focal classes` từ `10` repositories và vì vậy không cùng thang đo với ID 9. Test Wars (ID 21, ICST 2025) bổ sung góc nhìn so sánh LLM với EvoSuite, nhưng mutation chỉ là metric phụ và dataset là GitBug Java thay vì CLASSES2TEST. MutGen (ID 55, TSE 2026) là paper hiếm hoi đặt mutation score làm primary outcome, nhưng dùng benchmark synthetic thay vì real-world Java repositories.

### 2.3 Gap
GAP chính của đề tài là **GAP-M (Metric + Benchmark + Baseline)**. Trong 16 paper của evidence table, chỉ 4 paper báo cáo mutation score (ID 9, 12, 21, 55), nhưng không paper nào đồng thời thỏa mãn ba điều kiện: `(1)` mutation là primary outcome của RQ, `(2)` dataset là Java real-world class-level benchmark có thể đối chiếu trực tiếp với AgoneTest 2025, và `(3)` có baseline EvoSuite trên cùng benchmark đó. ID 9 là benchmark gần nhất nhưng mutation chỉ là metric phụ và chỉ so với human-written tests; ID 21 có EvoSuite baseline nhưng dùng dataset khác; ID 55 có mutation primary nhưng dataset synthetic. Vì vậy, khoảng trống nghiên cứu được chốt là: chưa có experiment nào đặt câu hỏi "GPT-4o-mini zero-shot tái lập được mức mutation/branch/build của AgoneTest 2025 trên cùng CLASSES2TEST universe ở mức sample-based replication không, và đứng ở đâu so với EvoSuite dưới một strict whole-sample view?" như một research claim được khóa protocol trước khi chạy.

### 2.4 Motivation
Nếu khoảng trống này không được giải quyết, cộng đồng sẽ tiếp tục phải so sánh các con số không cùng benchmark hoặc không cùng định nghĩa metric. Về thực tiễn, điều đó làm mờ đi câu hỏi quan trọng nhất: khi mang LLM-generated tests vào quy trình build thực tế, chúng có thật sự usable và có phát hiện lỗi tốt không.

## 3. Related Work

### 3.1 Overview

| Paper                       | Tool/LLM                                         | Dataset                                                                | Metric                                            | Key number                                                       | Limitation most relevant to this study                             |
| --------------------------- | ------------------------------------------------ | ---------------------------------------------------------------------- | ------------------------------------------------- | ---------------------------------------------------------------- | ------------------------------------------------------------------ |
| ID 9 - AgoneTest (2025)     | LLaMA 3.1-70B, GPT-4o-mini, Gemini-1.5-Pro       | CLASSES2TEST full class-level universe: 147,473 instances, 9,410 repos | Branch, line, method, mutation, build success     | GPT-4o-mini zero-shot: mutation 44.5%, branch 41.9%, build 28.6% | Coverage/mutation are compiled-only averages; no EvoSuite baseline |
| ID 12 - AgoneTest WS (2024) | GPT-4, GPT-3.5-turbo                             | CLASSES2TEST subset: 94 focal classes, 10 repos                        | Branch, line, method, instruction, mutation       | GPT-4 zero-shot mutation 54.6%, human 69.1%                      | Smaller subset, not same scale as ID 9                             |
| ID 21 - Test Wars (2025)    | TestSpark-ChatGPT-4o, EvoSuite, Kex              | GitBug Java, 136 bugs                                                  | Compile, line, branch, mutation, bug reproduction | EvoSuite best avg mutation 30.56%                                | Different benchmark from CLASSES2TEST                              |
| ID 55 - MutGen (2026)       | MutGen + LLaMA-3.3, GPT-4, DeepSeek-R1, EvoSuite | HumanEval-Java + LeetCode-Java                                         | Mutation, line, branch                            | Mutation 89.5% / 89.1%                                           | Synthetic subjects, not real-world repos                           |

### 3.2 Pattern Analysis
- Coverage vẫn là metric phổ biến nhất trong literature, còn mutation score vẫn hiếm và ít khi được dùng làm primary outcome.
- Dataset bị phân mảnh mạnh: CLASSES2TEST, GitBug Java, Defects4J, SF100, HumanEval-Java, LeetCode-Java, Pynguin benchmarks.
- Hai paper AgoneTest rất quan trọng nhưng khác nhau ở **thang đo benchmark**: ID 12 là subset `94/10`; ID 9 là full class-level universe và report build success trên full set.
- Nút thắt lặp lại là build/compilation failure. Vì vậy, study này phải tách rõ `AgoneTest-compatible` metrics với `strict whole-sample` metrics thay vì trộn hai định nghĩa.

### 3.3 Study Positioning
Study này được định vị là:
- **sampled replication** của AgoneTest 2025 trên cùng class-level CLASSES2TEST universe,
- **not** a replication của workshop 2024 subset,
- và đồng thời là **protocol extension** vì bổ sung so sánh EvoSuite trên cùng sample và báo cáo thêm strict whole-sample zero-fill view.

## 4. Research Questions

> Toàn bộ RQ, metric, threshold và statistical decision dưới đây được chốt trước khi chạy experiment. RQ1 là primary confirmatory claim theo literature-compatible protocol; RQ2-RQ4 là secondary pre-registered claims.

### RQ1 - Mutation Replication on the AgoneTest-Compatible Scale
**RQ1:** Trên một sample `N = 300` class-level instances được rút từ cùng CLASSES2TEST universe mà AgoneTest 2025 dùng, GPT-4o-mini zero-shot (`gpt-4o-mini-2024-07-18`) có tái lập được **compiled-only mutation score median >= 44.5%** hay không?

**H0:** GPT-4o-mini zero-shot **không** đạt compiled-only mutation score median >= 44.5%.  
**H1:** GPT-4o-mini zero-shot **đạt** compiled-only mutation score median > 44.5%.  
**Metric:** Mutation score (%) đo bằng PIT `pitest-maven` `DEFAULTS`, nhưng **chỉ trên các sampled instances mà generated suite đạt compilation success**, để khớp với cách đọc Table IV của ID 9.  
**Ngưỡng:** `44.5%`, lấy từ ID 9 (ASE 2025, Table IV), là compiled-only mutation score của GPT-4o-mini zero-shot.  
**Statistical test:** One-sample Wilcoxon signed-rank test, one-tailed, `alpha = 0.05`, trên tập compiled-only GPT rows. Nếu số compiled-success rows `< 60`, RQ1 chỉ được báo cáo descriptive.

### RQ2 - Branch Replication on the Same Scale
**RQ2:** Trên cùng sample `N = 300`, GPT-4o-mini zero-shot có tái lập được **compiled-only branch coverage median >= 41.9%** hay không?

**H0:** GPT-4o-mini zero-shot **không** đạt compiled-only branch coverage median >= 41.9%.  
**H1:** GPT-4o-mini zero-shot **đạt** compiled-only branch coverage median > 41.9%.  
**Metric:** Branch coverage (%) đo bằng JaCoCo `0.8.x`, chỉ trên sampled instances có compilation success theo protocol compatible với ID 9.  
**Ngưỡng:** `41.9%`, lấy từ ID 9 (ASE 2025, Table IV).  
**Statistical test:** One-sample Wilcoxon signed-rank test, one-tailed, `alpha = 0.05`. Nếu số compiled-success rows `< 60`, RQ2 chỉ được báo cáo descriptive.

### RQ3 - Build / Compilation Success Replication
**RQ3:** Trên cùng sample `N = 300`, GPT-4o-mini zero-shot có đạt **AgoneTest-compatible build/compilation success rate >= 28.6%** hay không?

**H0:** GPT-4o-mini zero-shot **không** đạt build/compilation success rate >= 28.6%.  
**H1:** GPT-4o-mini zero-shot **đạt** build/compilation success rate >= 28.6%.  
**Metric:** Tỷ lệ sampled instances mà generated suite tích hợp vào project và đi qua build/compilation gate của pipeline đánh giá. Đây là metric tương ứng với cột `Compilation` trong artifact released và Table V của ID 9.  
**Ngưỡng:** `28.6%`, lấy từ ID 9 (ASE 2025, Table V).  
**Statistical test:** Binomial exact test, one-tailed (`greater`), `p0 = 0.286`, `alpha = 0.05`.

### RQ4 - End-to-End Comparative Position versus EvoSuite
**RQ4:** Dưới strict whole-sample scoring, GPT-4o-mini zero-shot có **không kém EvoSuite quá 5 điểm phần trăm** về mutation score trên cùng sample `N = 300` hay không?

**H0:** Median chênh lệch per-instance `GPT - EvoSuite` về strict whole-sample mutation score < `-0.05`.  
**H1:** Median chênh lệch per-instance `GPT - EvoSuite` về strict whole-sample mutation score >= `-0.05`.  
**Metric:** Per-instance mutation score (%) với quy tắc zero-fill cho mọi build/metric failures của cả hai arms.  
**Statistical test:** One-sample Wilcoxon signed-rank test, one-tailed, trên `d_i = mutation_gpt_i - mutation_evosuite_i + 0.05`, `alpha = 0.05`; báo cáo thêm chênh lệch median và Vargha-Delaney `A12`.

## 5. Experiment Protocol

### 5.1 Study Type
Đây là một **sampled replication** của AgoneTest 2025, không phải census replication trên toàn bộ `147,473` instances. Lý do là full census vượt quá ngân sách thời gian 2 tuần của đề tài, trong khi sample-based replication từ cùng universe vẫn giữ được khả năng so sánh hợp lệ nếu sampling frame và metric definitions được giữ đúng.

### 5.2 Dataset and Sampling Frame
**Tên benchmark:** CLASSES2TEST  
**Published URL:** `https://anonymous.4open.science/r/classes2test`  
**Upstream parent:** `https://github.com/microsoft/methods2test`

**Universe used for comparison:** class-level CLASSES2TEST universe của ID 9, gồm `147,473` class-level instances từ `9,410` repositories theo Table II và released artifact.

**Sampling frame operationalization:**
1. Nếu `output/classes.csv` hoặc artifact tương đương có sẵn, dùng trực tiếp.
2. Nếu local mirror không đi kèm `classes.csv`, reconstruct frame bằng đúng rule của AgoneTest `extract.py`: dedupe mỗi instance theo `Project`, `Focal_Class`, `Test_Class`, `Focal_Path`, `Test_Path`.

**Study sample size:** `N = 300` class-level instances  
**Pilot size:** `N_pilot = 60` class-level instances  
**Sampling strategy:** stratified random sampling theo repository, seed `42`, với:
- `30` repositories
- `10` instances mỗi repository
- ít nhất `2` backup instances mỗi repository

**Replacement rule:** nếu một sampled instance không tái lập được buildability ở source project sau precheck, thay bằng backup tiếp theo trong cùng repository và log `replacement_of`.

**Lý do chọn N = 300:**
- vẫn lấy từ **cùng benchmark universe** với AgoneTest 2025,
- đủ lớn để kỳ vọng khoảng `~86` compiled-success GPT rows nếu build rate tương tự 28.6%,
- vẫn khả thi trong 2 tuần với một máy có thể chạy nhiều Maven jobs song song.

### 5.3 Pipeline tổng quan
1. Reconstruct hoặc load sampling frame của class-level CLASSES2TEST universe.
2. Randomly sample `N = 300` instances + backup pool, seed `42`.
3. Precheck buildability ở mức repository/module.
4. Sinh đúng **một** JUnit test class cho mỗi sampled instance bằng GPT-4o-mini zero-shot.
5. Chạy build/compilation gate và lưu raw logs.
6. Với các instances compile thành công, chạy JaCoCo và PIT để lấy AgoneTest-compatible metrics.
7. Chạy EvoSuite `1.2.0` trên cùng `N = 300` instances.
8. Tính thêm strict whole-sample zero-filled metrics cho cả hai arms.
9. Chạy statistical tests và xuất bảng per-instance + aggregate results.

### 5.4 LLM Configuration
**Model:** `gpt-4o-mini-2024-07-18`  
**Hyperparameters:** `temperature = 0`, `top_p = 1`, `max_output_tokens = 2048`, `frequency_penalty = 0`, `presence_penalty = 0`  
**Prompting strategy:** zero-shot  
**Prompt template (AgoneTest-compatible base zero-shot):**

```text
System:
You are provided with Java class. Create a test class that fully tests the proposed Java class using the project information for imports. Reply with code only, do not add other text that is not code.

User:
The project uses {testing_framework} and Java {java_version} and Java class is:
<code>
{class_under_test}
</code>
```

**Lock:** Không dùng enhanced prompt có `project_structure` và `project_dependencies` cho RQ1-RQ3, vì như vậy sẽ lệch khỏi scale reference của Table IV/Table V trong ID 9.

### 5.5 Measurement

| Metric family               | Tool                                         | Definition in this study                                                                  | Used for                    |
| --------------------------- | -------------------------------------------- | ----------------------------------------------------------------------------------------- | --------------------------- |
| Compilation / build success | Maven/Gradle build + project compiler plugin | Binary per-instance gate aligned to `Compilation` semantics in AgoneTest artifact         | RQ3                         |
| Branch coverage             | JaCoCo `0.8.x`                               | Compiled-only branch coverage; plus zero-filled branch for extension tables               | RQ2 + descriptive extension |
| Line coverage               | JaCoCo `0.8.x`                               | Compiled-only line coverage; plus zero-filled line for extension tables                   | Descriptive                 |
| Mutation score              | PIT `pitest-maven` `1.15.x`, `DEFAULTS`      | Compiled-only mutation for direct replication; zero-filled mutation for strict comparison | RQ1 + RQ4                   |

**Measurement rule split:**
- **AgoneTest-compatible layer:** build/compilation on full sample; branch/line/mutation summarized only over successful compilation rows.
- **Strict extension layer:** any GPT or EvoSuite failure that prevents metric computation receives `0.0` cho metric bị ảnh hưởng, và `fail_stage` được log rõ ràng.

### 5.6 Baseline
**Baseline:** EvoSuite `1.2.0`  
**Configuration:** `search_budget = 60s/instance`, `criterion = branch`  
**Reason:** CLASSES2TEST currently has no published EvoSuite comparison. This arm supplies the missing comparative baseline while keeping the same sampled instances and the same measurement pipeline.

### 5.7 Statistical Analysis Plan
**RQ1:** One-sample Wilcoxon signed-rank, one-tailed, `mu0 = 44.5`, on compiled-only mutation scores.  
**RQ2:** One-sample Wilcoxon signed-rank, one-tailed, `mu0 = 41.9`, on compiled-only branch scores.  
**RQ3:** Exact binomial test, one-tailed, `p0 = 0.286`, on compilation success across all `N = 300` instances.  
**RQ4:** One-sample Wilcoxon signed-rank on margin-shifted per-instance difference `d_i = GPT_i - EvoSuite_i + 0.05`, one-tailed.  

**Effect size plan:**
- RQ1, RQ2, RQ4: rank-biserial effect size + observed median difference
- RQ3: observed proportion difference + Wilson CI

**Power and feasibility safeguard:**
- Full run target là `N = 300`.
- Nếu pilot hoặc precheck cho thấy fewer than `20` repositories are reproducibly buildable với thời gian hiện có, team có thể amend downward to `N = 200`, nhưng chỉ trước khi batch GPT generation bắt đầu.
- Nếu GPT compiled-success rows sau full run `< 60`, RQ1-RQ2 chỉ được báo cáo descriptive và không diễn giải như fail-to-reject confirmatory claim.

## 6. Evaluation Plan

### 6.1 Decision Table

| RQ  | Metric                                       | Comparison target | Protocol layer       | Decision rule                                                     |
| --- | -------------------------------------------- | ----------------- | -------------------- | ----------------------------------------------------------------- |
| RQ1 | Median mutation score                        | `>= 44.5%`        | AgoneTest-compatible | Reject H0 khi `p < 0.05` và observed median `> 44.5`              |
| RQ2 | Median branch coverage                       | `>= 41.9%`        | AgoneTest-compatible | Reject H0 khi `p < 0.05` và observed median `> 41.9`              |
| RQ3 | Build/compilation success rate               | `>= 28.6%`        | AgoneTest-compatible | Reject H0 khi `p < 0.05` và observed proportion `>= 0.286`        |
| RQ4 | Per-instance mutation difference vs EvoSuite | `>= -5 pp`        | Strict whole-sample  | Reject H0 khi `p < 0.05` và observed median difference `>= -0.05` |

### 6.2 Interpretation Logic
- **RQ1+/RQ2+/RQ3+:** sample-based replication successfully reproduces the AgoneTest 2025 scale on the same benchmark universe.
- **RQ3+ but RQ1-/RQ2-:** GPT compiles often enough, but quality among successful suites is below the published benchmark.
- **RQ1+/RQ2+ but RQ3-:** successful suites look good, nhưng end-to-end practical usability yếu hơn AgoneTest 2025.
- **RQ4+:** dưới zero-fill end-to-end lens, GPT vẫn cạnh tranh với EvoSuite trên sampled CLASSES2TEST instances.

## 7. Threats to Validity

### 7.1 Internal Validity
**Threat:** Cloud LLM có thể bị silent-update hoặc drift theo thời gian.  
**Mitigation:** Pin `gpt-4o-mini-2024-07-18`, log timestamp, raw response, token usage, retry history, và không đổi prompt sau khi chạy pilot.

**Threat:** Local pipeline có thể không khớp 100% với original AgoneTest environment.  
**Mitigation:** Dùng released prompt scale, cùng benchmark universe definition, và tách rõ compatible metrics khỏi strict extension metrics.

### 7.2 External Validity
**Threat:** Study là Java-only và sample-based chứ không phải full census.  
**Mitigation:** Đóng khung claim ở mức sampled replication on CLASSES2TEST universe, không khái quát sang Python hay mọi Java benchmark khác.

### 7.3 Construct Validity
**Threat:** `44.5 / 41.9 / 28.6` đến từ hai summary khác nhau trong ID 9: compiled-only averages và full-set build rate.  
**Mitigation:** Encode đúng split này trong protocol thay vì áp một threshold lên denominator khác.

**Threat:** Zero-filled metrics nghiêm ngặt hơn literature-compatible metrics.  
**Mitigation:** Dùng zero-fill cho extension layer và EvoSuite comparison, không dùng nó để threshold trực tiếp với Table IV.

### 7.4 Conclusion Validity
**Threat:** Build failures làm giảm analyzable `n` cho compiled-only metrics.  
**Mitigation:** Set `N = 300`, pre-register descriptive fallback nếu compiled-success rows `< 60`, và luôn báo cáo uncertainty.

## 8. Timeline & Resources

### 8.1 Roles

| Role | Thành viên | Trách nhiệm                                                              |
| ---- | ---------- | ------------------------------------------------------------------------ |
| PL   | Member 1   | Khóa scope, điều phối amendment, kiểm tra nhất quán tài liệu             |
| DG   | Member 2   | Reconstruct sampling frame, stage dataset, sampling log, replacement log |
| LR   | Member 3   | API setup, GPT generation pipeline, request/response logging             |
| MS   | Member 4   | JaCoCo/PIT/EvoSuite pipeline, stats scripts, verification                |
| RW   | Member 5   | Viết proposal/report, figures, consistency editing                       |

### 8.2 Resource Inventory

| Tài nguyên          | Trạng thái | Ghi chú                                                                             |
| ------------------- | ---------- | ----------------------------------------------------------------------------------- |
| CLASSES2TEST mirror | ⚠️          | Local mirror đã có, nhưng cần reconstruct sampling frame nếu `classes.csv` vắng mặt |
| OpenAI API key      | ⚠️          | Cần tạo project riêng và khóa budget cap                                            |
| Java toolchain      | ✅          | Maven, JaCoCo, PIT, EvoSuite đều là public tools                                    |
| Compute             | ✅          | Một máy local có thể chạy batch `N = 300` nếu song song ở mức vừa phải              |

### 8.3 Cost Estimate

| Item                          | Estimate                                      |
| ----------------------------- | --------------------------------------------- |
| GPT-4o-mini API for `N = 300` | expected `< USD 2.00`, budget cap `USD 10.00` |
| EvoSuite / JaCoCo / PIT       | `USD 0.00`                                    |
| Local compute and storage     | `USD 0.00`                                    |

> Ghi chú: Dự toán trên dùng sample-based replication `N = 300`, không phải full census `147,473`.

### 8.4 Two-Week Execution Plan

| Day range | Hoạt động                                                     | Output                                       |
| --------- | ------------------------------------------------------------- | -------------------------------------------- |
| Day 1-2   | Reconstruct sampling frame, sample `N = 300`, freeze versions | `data/full_sample_manifest.csv`, version log |
| Day 3     | Pilot `N_pilot = 60` smoke run                                | `results/pilot_*.csv`, issue log             |
| Day 4-6   | GPT arm batch run                                             | `results/full_llm_output.csv`                |
| Day 7-9   | JaCoCo/PIT parsing + reruns cho failures cần xác minh         | `results/full_metrics_gpt.csv`               |
| Day 10-12 | EvoSuite arm + metrics                                        | `results/full_metrics_evosuite.csv`          |
| Day 13    | Statistical analysis + figures                                | `results/full_analysis.*`                    |
| Day 14    | Write-up + consistency audit                                  | final tables and narrative                   |

### 8.5 Amendment Rule
Amendment chỉ hợp lệ nếu xảy ra trước batch generation và vì lý do kỹ thuật, ví dụ:
- sampling frame reconstruct không ổn định,
- số repository buildable thấp hơn ngưỡng an toàn,
- metric pipeline không tái lập được artifact semantics của AgoneTest.

Các thay đổi không hợp lệ gồm:
- đổi threshold sau khi thấy kết quả,
- đổi prompt để “cứu” một batch đã chạy,
- chuyển từ AgoneTest-compatible sang zero-fill primary analysis sau khi đã nhìn số liệu.
