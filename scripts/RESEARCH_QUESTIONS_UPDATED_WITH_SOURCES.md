# Research Questions cập nhật có nguồn trích dẫn

Tài liệu này chốt lại bộ Research Questions (RQ) cho thực nghiệm so sánh GPT-4o-mini zero-shot và EvoSuite trên 300 focal class Java. Mục tiêu là giữ tính liêm chính khoa học: RQ nào dùng để tái lập AgoneTest 2025 phải dùng đúng thang đo AgoneTest-compatible; RQ nào là phần mở rộng so sánh với EvoSuite hoặc phân tích lỗi phải được ghi rõ là protocol extension hoặc secondary/exploratory.

## 1. Vai trò của bản cập nhật

Bản này không thay đổi khoảng trống nghiên cứu chính đã ghi trong `proposal.md`. Khoảng trống chính vẫn là:

> Chưa có nghiên cứu nào đồng thời dùng benchmark Java real-world class-level cùng họ với CLASSES2TEST, báo cáo mutation score, và so sánh trực tiếp GPT với EvoSuite trên cùng focal classes bằng cùng pipeline đo.

Điểm được cập nhật là cách diễn đạt RQ để paper rõ hơn:

- Nhóm RQ tái lập AgoneTest 2025: kiểm tra GPT-4o-mini zero-shot có tái lập được mức mutation, branch và build success đã công bố hay không.
- Nhóm RQ so sánh trực tiếp GPT với EvoSuite: đặt hai công cụ trên cùng 300 focal class, dùng strict whole-sample scoring để tránh bias do chỉ nhìn các test build được.
- Nhóm RQ giải thích: phân tích ảnh hưởng của cyclomatic complexity (CC), build tool, và taxonomy lỗi để chỉ ra khi nào GPT hoặc EvoSuite mạnh/yếu.

Nếu bản này được khóa trước full run 300 class chính thức, có thể dùng RQ1 đến RQ5 như confirmatory hoặc secondary pre-registered claims. RQ6 và RQ7 nên giữ là exploratory/diagnostic vì chúng phục vụ giải thích và định hướng nghiên cứu tiếp theo.

## 2. Nguồn paper dùng để neo RQ

| Mã nguồn | Paper | Bằng chứng dùng trong nghiên cứu này | Vì sao chọn | Giới hạn khi dùng làm tham chiếu |
|---|---|---|---|---|
| P1 | Andrea Lops et al., "LLMs for Automated Unit Test Generation and Assessment in Java: The AgoneTest Framework", ASE 2025. Local: `SLR/papers/AndreaLops_2025_TheAGONETESTFramework.pdf`. Online: https://arxiv.org/abs/2511.20403 | AgoneTest giới thiệu CLASSES2TEST và pipeline đánh giá class-level với build success, coverage, mutation score và test smells. Proposal đang dùng các mốc GPT-4o-mini zero-shot: branch `41.9%`, line `64.8%`, method `77.2%`, mutation `44.5%`, build success `28.6%`. | Đây là paper gần nhất với mục tiêu của đề tài vì cùng bài toán Java class-level, cùng hướng AgoneTest, cùng loại metric cần tái lập. | Paper này chưa có EvoSuite baseline trên cùng benchmark, và các số coverage/mutation là compiled-only nên không được trộn trực tiếp với strict zero-fill. |
| P2 | Andrea Lops et al., "A System for Automated Unit Test Generation Using Large Language Models and Assessment of Generated Test Suites", ICSTW 2025/Workshop 2024 preprint. Local: `SLR/papers/AndreaLops_2024_ASystemforAutomatedUnitTestGenerationUsingLargeLanguageModelsandAssessmentofGeneratedTestSuites.pdf`. Online: https://arxiv.org/abs/2408.07846 | Paper trình bày AgoneTest như hệ thống sinh và đánh giá test suite Java ở mức class-level, với dataset nhỏ hơn: 94 focal classes từ 10 repositories; proposal ghi GPT-4 zero-shot mutation `54.6%`, human `69.1%`. | Dùng để chứng minh hướng đánh giá class-level và mutation/coverage không phải tự phát minh; cũng là tiền thân trực tiếp của AgoneTest 2025. | Không nên dùng `54.6%` làm ngưỡng chính cho GPT-4o-mini vì model, sample size và dataset scale khác P1. |
| P3 | Azat Abdullin et al., "Test Wars: A Comparative Study of SBST, Symbolic Execution, and LLM-Based Approaches to Unit Test Generation", 2025. Local: `SLR/papers/AzatAbdullin_2025_AComparativeStudyofSBSTSymbolicExecutionandLLM-BasedApproachestoUnitTestGeneration.pdf`. Online: https://arxiv.org/html/2501.10200v1 | Paper so sánh EvoSuite, Kex và TestSpark-LLM trên GitBug Java; đánh giá compile rate, coverage, mutation score, complexity và phân tích strengths/weaknesses của từng approach. Proposal ghi EvoSuite best average mutation `30.56%`. | Đây là nguồn mạnh để biện minh RQ so sánh GPT với EvoSuite, RQ buildability, RQ theo CC, và định hướng phân tích ưu/nhược điểm. | Dataset là GitBug Java, không phải CLASSES2TEST. Vì vậy số `30.56%` chỉ dùng làm mốc ngữ cảnh, không dùng làm threshold kiểm định chính. |
| P4 | Yutian Tang et al., "ChatGPT vs SBST: A Comparative Assessment of Unit Test Suite Generation", IEEE TSE 2024. Local: `SLR/papers/YutianTang_2023_AComparativeAssessmentofUnitTestSuiteGeneration.pdf`. Online: https://doi.org/10.1109/TSE.2024.3382365 | Paper so sánh ChatGPT với EvoSuite theo correctness, readability, code coverage và bug detection capability, nhấn mạnh strengths/weaknesses của LLM so với SBST. | Dùng để củng cố rằng so sánh GPT với EvoSuite là hướng nghiên cứu đã được cộng đồng công nhận, nhưng cần mở rộng sang CLASSES2TEST và mutation/build end-to-end. | Không cùng setup AgoneTest, không dùng cùng dataset, nên chỉ là nền tảng lý thuyết cho RQ so sánh. |
| P5 | Guancheng Wang et al., "Mutation-Guided Unit Test Generation with a Large Language Model", 2026. Local: `SLR/papers/GuanchengWang_2026_Mutation-GuidedUnitTestGenerationwithaLargeLanguageModel.pdf` | Proposal ghi MutGen cho thấy coverage cao có thể vẫn có mutation score thấp; paper đặt mutation làm outcome trung tâm hơn nhiều nghiên cứu LLM-test khác. | Dùng để biện minh vì sao mutation score phải là metric chính, không chỉ line/branch coverage. | Benchmark synthetic như HumanEval-Java/LeetCode-Java, không đại diện trực tiếp cho real-world repositories. |
| P6 | Caroline Lemieux et al., "CodaMOSA: Escaping Coverage Plateaus in Test Generation with Pre-trained Large Language Models", ICSE 2023. Local: `SLR/papers/CarolineLemieux_2023_EscapingCoveragePlateausinTestGenerationwithPre-trainedLargeLanguageModels.pdf`. Online: https://www.microsoft.com/en-us/research/publication/codamosa-escaping-coverage-plateaus-in-test-generation-with-pre-trained-large-language-models/ | CodaMOSA là bằng chứng sớm rằng LLM và SBST có thể bổ trợ nhau trong test generation. | Dùng để giải thích đóng góp dài hạn: kết quả GPT vs EvoSuite có thể làm nền cho paper tiếp theo về hybrid GPT + EvoSuite. | Không dùng làm baseline trực tiếp vì CodaMOSA không phải setup AgoneTest Java class-level hiện tại. |

## 3. Bộ Research Questions cập nhật

### RQ1 - Mutation replication theo thang AgoneTest-compatible

**RQ1:** Trên sample 300 focal class lấy từ cùng hướng CLASSES2TEST, GPT-4o-mini zero-shot có tái lập được mức compiled-only mutation score của AgoneTest 2025 hay không?

**Nguồn chọn RQ:** P1 báo cáo GPT-4o-mini zero-shot đạt mutation score `44.5%` trên tập test compile được. P5 củng cố lý do chọn mutation score vì coverage cao chưa chắc đồng nghĩa khả năng phát hiện lỗi tốt.

**Giả thuyết:**

- **H0:** Median compiled-only mutation score của GPT-4o-mini không vượt mốc `44.5%`.
- **H1:** Median compiled-only mutation score của GPT-4o-mini vượt mốc `44.5%`.

**Metric và kiểm định:**

- Metric chính: PIT mutation score trên các GPT-generated tests build/compile thành công.
- Test: one-sample Wilcoxon signed-rank, one-tailed, `alpha = 0.05`, `mu0 = 44.5`.
- Nếu số row GPT compile thành công `< 60`, chỉ báo cáo descriptive, không chốt confirmatory.

**Tại sao chọn:** Mutation score đo khả năng test phát hiện mutant, sát mục tiêu "test có bắt lỗi không" hơn coverage đơn thuần.

**Đóng góp:** Cho biết kết quả GPT-4o-mini trong môi trường thí nghiệm độc lập có tái lập được mốc AgoneTest 2025 không, thay vì chỉ dựa vào claim công bố.

### RQ2 - Branch coverage replication theo thang AgoneTest-compatible

**RQ2:** Trên cùng sample, GPT-4o-mini zero-shot có tái lập được mức compiled-only branch coverage của AgoneTest 2025 hay không?

**Nguồn chọn RQ:** P1 báo cáo branch coverage `41.9%` cho GPT-4o-mini zero-shot trên compiled-only rows. Branch coverage cũng là metric quen thuộc trong SBST và phù hợp khi so sánh với EvoSuite.

**Giả thuyết:**

- **H0:** Median compiled-only branch coverage của GPT-4o-mini không vượt mốc `41.9%`.
- **H1:** Median compiled-only branch coverage của GPT-4o-mini vượt mốc `41.9%`.

**Metric và kiểm định:**

- Metric chính: JaCoCo branch coverage trên các GPT-generated tests build/compile thành công.
- Test: one-sample Wilcoxon signed-rank, one-tailed, `alpha = 0.05`, `mu0 = 41.9`.
- Nếu số row GPT compile thành công `< 60`, chỉ báo cáo descriptive.

**Tại sao chọn:** Branch coverage giúp đối chiếu với AgoneTest 2025 và giúp giải thích quan hệ giữa coverage và mutation score.

**Đóng góp:** Là lớp kiểm tra chất lượng truyền thống, giúp paper không chỉ nói về mutation mà còn so sánh với metric phổ biến trong testing literature.

### RQ3 - Build/compilation success replication

**RQ3:** GPT-4o-mini zero-shot có đạt build/compilation success rate tương đương hoặc cao hơn mốc AgoneTest 2025 trên toàn bộ 300 focal class không?

**Nguồn chọn RQ:** P1 báo cáo build/compilation success `28.6%`. Các kết quả pilot của tool cũng cho thấy buildability là điểm nghẽn thực tế, nên không thể chỉ nhìn compiled-only quality.

**Giả thuyết:**

- **H0:** Build/compilation success rate của GPT-4o-mini thấp hơn `28.6%`.
- **H1:** Build/compilation success rate của GPT-4o-mini đạt ít nhất `28.6%`.

**Metric và kiểm định:**

- Metric chính: tỷ lệ class mà generated test build/compile thành công và có thể đưa vào bước đo JaCoCo/PIT.
- Test: exact binomial test, one-tailed, `p0 = 0.286`, `alpha = 0.05`.
- Báo cáo thêm Wilson confidence interval cho tỷ lệ quan sát.

**Tại sao chọn:** Một test suite sinh ra có mutation cao nhưng không build được trên đa số class thì giá trị thực dụng thấp.

**Đóng góp:** Bảo vệ tính liêm chính của kết luận bằng cách tách "quality khi đã compile" khỏi "khả năng dùng được end-to-end".

### RQ4 - So sánh strict mutation score giữa GPT và EvoSuite

**RQ4:** Dưới strict whole-sample scoring, GPT-4o-mini zero-shot có không kém EvoSuite quá 5 điểm phần trăm về mutation score trên cùng 300 focal class không?

**Nguồn chọn RQ:** P1 thiếu EvoSuite baseline trên CLASSES2TEST. P3 và P4 cho thấy so sánh LLM với EvoSuite/SBST là hướng nghiên cứu được công nhận, nhưng đang thiếu setup cùng benchmark AgoneTest. Vì vậy RQ4 là phần mở rộng trực tiếp của proposal.

**Giả thuyết:**

- **H0:** Median chênh lệch strict mutation `GPT - EvoSuite` nhỏ hơn `-5 pp`.
- **H1:** Median chênh lệch strict mutation `GPT - EvoSuite` lớn hơn hoặc bằng `-5 pp`.

**Metric và kiểm định:**

- Metric chính: strict mutation score theo từng focal class.
- Quy tắc strict: nếu generated test không sinh được, không build được, hoặc không đo được PIT vì lỗi test sinh ra, mutation score của arm đó bằng `0`.
- Test: paired Wilcoxon trên `d_i = mutation_gpt_i - mutation_evosuite_i + 0.05`, one-tailed, `alpha = 0.05`.
- Báo cáo thêm median paired difference, rank-biserial effect size, Vargha-Delaney A12.
- Margin `5 pp` là ngưỡng non-inferiority do protocol định nghĩa, không lấy trực tiếp từ paper. Nên báo cáo sensitivity với margin `0 pp`, `5 pp`, `10 pp`.

**Tại sao chọn:** Đây là câu hỏi gần nhất với mục tiêu của đề tài: GPT có thực sự cạnh tranh được với EvoSuite trong thực nghiệm end-to-end hay không.

**Đóng góp:** Bổ sung EvoSuite baseline cho sample CLASSES2TEST, lấp khoảng trống mà AgoneTest 2025 chưa xử lý.

### RQ5 - So sánh buildability giữa GPT và EvoSuite

**RQ5:** Trên cùng 300 focal class, GPT-4o-mini zero-shot và EvoSuite khác nhau như thế nào về tỷ lệ generated test build/compile thành công?

**Nguồn chọn RQ:** P1 coi build/compilation success là metric báo cáo quan trọng. P3 so sánh các tool bằng compilation rate, coverage và mutation score. P4 cũng đặt correctness và khả năng dùng được của generated tests vào trọng tâm so sánh ChatGPT với EvoSuite.

**Giả thuyết:**

- **H0:** Không có khác biệt paired đáng kể giữa GPT và EvoSuite về build/compilation success.
- **H1:** Có khác biệt paired đáng kể giữa GPT và EvoSuite về build/compilation success.

**Metric và kiểm định:**

- Metric chính: binary success/fail theo từng arm và từng focal class.
- Test: McNemar exact test trên bảng paired success/fail.
- Báo cáo thêm paired odds ratio, success rate từng arm, và breakdown theo Maven/Gradle/TestNG/JUnit nếu đủ dữ liệu.

**Tại sao chọn:** Nếu GPT fail build nhiều hơn, compiled-only metric sẽ làm GPT trông mạnh hơn thực tế; nếu EvoSuite fail sinh test nhiều hơn, strict metric cũng phải ghi nhận công bằng.

**Đóng góp:** Đưa buildability thành kết quả khoa học riêng, không xem nó chỉ là lỗi kỹ thuật bị bỏ qua.

### RQ6 - Ảnh hưởng của cyclomatic complexity đến GPT và EvoSuite

**RQ6:** Độ phức tạp cyclomatic complexity của focal class có ảnh hưởng đến build success, coverage và mutation score của GPT/EvoSuite không?

**Nguồn chọn RQ:** P3 đánh giá performance theo các đặc trưng của code under test, gồm complexity và size. P1/P2 đánh giá class-level test generation trên real-world Java classes, nơi độ phức tạp class có thể làm test generation khó hơn. RQ này phù hợp với dữ liệu của đề tài vì manifest có các trường CC hoặc có thể đo lại bằng công cụ chuẩn.

**Trạng thái RQ:** Secondary/exploratory, trừ khi được khóa trước full run.

**Metric và phân tích:**

- Nhóm CC: ưu tiên dùng tertile hoặc predefined bins đã khóa trước khi chạy full: `low`, `medium`, `high`.
- Metric: build success, branch coverage, mutation score strict và compiled-only.
- Phân tích: descriptive theo nhóm CC; logistic regression cho build success với công thức `success ~ arm + cc_group + arm:cc_group`; paired comparison GPT vs EvoSuite trong từng nhóm CC.

**Tại sao chọn:** Average toàn bộ 300 class có thể che mất pattern quan trọng: EvoSuite có thể mạnh ở class thuần logic nhưng yếu khi môi trường/mock phức tạp; GPT có thể viết oracle tốt ở class đơn giản nhưng fail import/API ở repo khó.

**Đóng góp:** Trả lời "GPT mạnh hơn EvoSuite ở đâu" thay vì chỉ trả lời "ai thắng trung bình".

### RQ7 - Taxonomy lỗi và ưu/nhược điểm thực nghiệm

**RQ7:** Các lỗi khiến GPT hoặc EvoSuite không đo được đến từ đâu: repo/environment, build tool harness, generator, generated test compile failure, hay PIT/JaCoCo measurement?

**Nguồn chọn RQ:** P3 nhấn mạnh phân tích strengths/weaknesses của từng approach và dùng kết quả đó để định hướng hybrid techniques. P1/P2 có pipeline AgoneTest end-to-end, nhưng chưa cung cấp taxonomy lỗi chi tiết giữa GPT và EvoSuite trên cùng sample như nghiên cứu này cần.

**Trạng thái RQ:** Exploratory/diagnostic, không phải hypothesis chính.

**Metric và phương pháp:**

- Dùng các trường log: `stage`, `fail_stage`, `status`, `issue`, `failure_owner`, `owner_note_vi`, `build_tool`, `repo_id`, `class_name`.
- Nhóm lỗi tối thiểu:
  - `repo_or_environment`: repo không đủ dependency, JDK/toolchain thiếu, Maven/Gradle metadata chết.
  - `agonetest_harness`: pipeline gốc AgoneTest không hỗ trợ đúng module/build tool trong local setup.
  - `gpt_generated_test`: GPT sinh test không compile, sai import, sai API, sai framework.
  - `evosuite_engine`: EvoSuite crash, không sinh test, test phụ thuộc runtime không ổn định.
  - `measurement`: PIT/JaCoCo không chạy được sau khi test đã build.
- Báo cáo count, percentage, và ví dụ đại diện từng nhóm.

**Tại sao chọn:** Khi test không đo được, không thể kết luận "GPT yếu" hay "EvoSuite yếu" nếu chưa tách được lỗi do tool pipeline, repo, hoặc generator.

**Đóng góp:** Tạo evidence để paper có phần discussion mạnh hơn và giúp thiết kế nghiên cứu tiếp theo về tích hợp GPT + EvoSuite.

## 4. Cách kết luận H0/H1 sau full run

| RQ | Loại kết luận | Điều kiện reject H0 | Diễn giải khi reject H0 | Diễn giải khi không reject H0 |
|---|---|---|---|---|
| RQ1 | Confirmatory replication | `p < 0.05` và observed median mutation `> 44.5%` | GPT tái lập/vượt mốc mutation AgoneTest 2025 trên compiled-only rows. | Không đủ bằng chứng GPT tái lập mốc mutation; nếu compile success thấp, nhấn mạnh giới hạn practical usability. |
| RQ2 | Confirmatory replication | `p < 0.05` và observed median branch `> 41.9%` | GPT tái lập/vượt mốc branch coverage AgoneTest 2025. | Không đủ bằng chứng GPT tái lập mốc branch coverage. |
| RQ3 | Confirmatory replication | `p < 0.05` và observed build rate `>= 28.6%` | GPT đạt/vượt mức buildability của AgoneTest 2025. | GPT chưa đạt mức buildability tham chiếu; compiled-only score phải được diễn giải rất thận trọng. |
| RQ4 | Secondary confirmatory extension | `p < 0.05` và median paired difference `>= -5 pp` | GPT không kém EvoSuite quá margin đã khóa về strict mutation. | Không đủ bằng chứng GPT cạnh tranh với EvoSuite theo strict end-to-end mutation. |
| RQ5 | Secondary extension | McNemar exact `p < 0.05` | Hai tool có khác biệt đáng kể về buildability; direction dựa vào paired table. | Không đủ bằng chứng có khác biệt buildability giữa GPT và EvoSuite. |
| RQ6 | Exploratory | Không chốt H0/H1 chính nếu chưa pre-register | Dùng để giải thích kết quả theo nhóm độ phức tạp. | Không có pattern rõ hoặc sample từng nhóm chưa đủ mạnh. |
| RQ7 | Diagnostic | Không dùng kiểm định H0/H1 | Tạo taxonomy lỗi và phân biệt lỗi do repo/tool/generator. | Nếu log thiếu, chỉ báo cáo descriptive và nêu limitation. |

## 5. Vì sao không dùng trực tiếp mọi số paper làm threshold

Không phải paper nào có số liệu cũng có thể biến thành ngưỡng kiểm định. Nghiên cứu này chỉ dùng trực tiếp các ngưỡng từ P1 cho RQ1 đến RQ3 vì P1 là mốc gần nhất: cùng hướng AgoneTest, cùng GPT-4o-mini zero-shot, cùng class-level Java evaluation. Các paper khác dùng để tạo nền và giải thích:

- P2 dùng làm tiền thân AgoneTest và xác nhận class-level evaluation là hướng hợp lệ, nhưng không dùng số `54.6%` làm threshold vì dùng GPT-4 và subset nhỏ.
- P3 dùng để biện minh so sánh với EvoSuite, buildability, CC và strengths/weaknesses, nhưng không dùng `30.56%` của EvoSuite làm threshold vì dataset GitBug khác CLASSES2TEST.
- P4 dùng để củng cố hướng ChatGPT vs EvoSuite là hướng đã có trong literature, nhưng không phải replication target.
- P5 dùng để biện minh mutation score quan trọng hơn coverage đơn thuần, nhưng benchmark synthetic nên không dùng làm mốc thực nghiệm chính.
- P6 dùng để định hướng future work hybrid GPT + EvoSuite, không dùng làm RQ chính trong full 300 hiện tại.

## 6. Đóng góp khoa học kỳ vọng

Nghiên cứu này không chỉ nhằm nói "GPT mạnh hơn EvoSuite" hoặc "EvoSuite mạnh hơn GPT". Đóng góp hợp lý hơn là:

1. **Sampled replication của AgoneTest 2025:** kiểm tra GPT-4o-mini zero-shot có tái lập được mốc mutation/branch/build trên sample độc lập hay không.
2. **EvoSuite baseline trên cùng sample:** bổ sung cánh so sánh mà AgoneTest 2025 chưa có.
3. **Strict whole-sample view:** tránh kết luận quá đẹp từ compiled-only subset bằng cách tính fail thành `0` ở lớp so sánh end-to-end.
4. **Buildability là metric nghiên cứu chính thức:** ghi nhận test sinh ra có dùng được trong repo thật hay không.
5. **Complexity-aware analysis:** cho thấy hiệu năng thay đổi theo độ phức tạp focal class, thay vì chỉ báo cáo trung bình tổng.
6. **Failure taxonomy:** phân biệt lỗi repo/environment, lỗi harness, lỗi generator, lỗi EvoSuite engine và lỗi measurement.
7. **Nền cho nghiên cứu hybrid:** nếu GPT và EvoSuite có lỗi/phần mạnh khác nhau theo class hoặc CC, kết quả này là bằng chứng để thiết kế pipeline kết hợp GPT + EvoSuite trong paper tiếp theo.

## 7. Mapping với file kết quả của tool

Khi full run xong, mỗi RQ nên lấy số liệu từ các file sau:

| RQ | File chính | Cột/trường cần dùng |
|---|---|---|
| RQ1 | `metrics_long.csv`, `metrics_summary.csv` | `arm == gpt`, `build_success`, `mutation_score`, compiled-only filter |
| RQ2 | `metrics_long.csv`, `metrics_summary.csv` | `arm == gpt`, `branch_coverage`, compiled-only filter |
| RQ3 | `metrics_long.csv`, `error_summary.csv` | `arm == gpt`, `build_success`, `status`, `fail_stage` |
| RQ4 | `metrics_long.csv` | paired strict `mutation_score` của `gpt` và `evosuite` theo `sample_index/class_key` |
| RQ5 | `metrics_long.csv`, `error_owner_summary.csv` | paired binary success/fail theo arm; breakdown `failure_owner` |
| RQ6 | manifest final + `metrics_long.csv` | `cc_group`, `cyclomatic_complexity`, `arm`, `success`, `branch_coverage`, `mutation_score` |
| RQ7 | `error_summary.csv`, `generated_failures.csv`, `error_status_by_class.csv` | `failure_owner`, `issue`, `fail_stage`, `owner_note_vi`, sample examples |

## 8. Kết luận định hướng cho paper

Nếu full 300 cho kết quả gần giống pilot hiện tại, kết luận nên viết theo hướng cân bằng:

- GPT có thể rất mạnh trên các test build được, đặc biệt về mutation/oracle quality.
- EvoSuite có thể ổn định hơn ở buildability hoặc coverage end-to-end trong một số nhóm class.
- Vì build failure ảnh hưởng lớn, kết luận "GPT mạnh hơn EvoSuite" chỉ hợp lệ nếu strict whole-sample metric hoặc buildability-adjusted analysis cũng ủng hộ.
- Nếu GPT compiled-only cao nhưng build rate thấp, đóng góp chính không phải là GPT đã thắng tuyệt đối, mà là chứng minh khoảng cách giữa potential quality và practical usability.
- Nếu hai tool mạnh/yếu khác nhau theo CC hoặc failure mode, hướng nghiên cứu có giá trị nhất là pipeline hybrid chọn hoặc sửa test theo điều kiện class.

## 9. Tài liệu liên quan trong project

- `D:\A_ThucNghiem\proposal.md`: proposal gốc và ngưỡng RQ ban đầu.
- `D:\A_ThucNghiem\scripts_v2\proposal.md`: bản proposal đi kèm tool portable.
- `D:\A_ThucNghiem\scripts_v2\results\runs\<run_id>\metrics_long.csv`: dữ liệu metric long-form cho kiểm định.
- `D:\A_ThucNghiem\scripts_v2\results\runs\<run_id>\error_summary.csv`: bảng lỗi đã phân loại.
- `D:\A_ThucNghiem\scripts_v2\results\runs\<run_id>\error_owner_summary.csv`: tổng hợp lỗi theo owner.
