# Phân tích pilot 60 class - run 20260717_084920_5f360ff0

Báo cáo này được tạo từ các file kết quả trong `results/runs/20260717_084920_5f360ff0`, có đối chiếu với `proposal.md` và các giá trị tham khảo của paper AgoneTest 2025.

## 1. Kết luận nhanh

- Pilot đã hoàn tất theo `manifest.json`: `completed`, `error=null`. `status.json` vẫn ghi `running` nhưng process `pid=17148` không còn; đây là trạng thái stale của file status/UI, không phải run còn chạy.
- Dữ liệu đầu vào hợp lệ: 60/60 class qua precheck, 0 class bị skip, 33 repo được chạy, 41 module baseline build đều `PASS`.
- Repo/module sạch không phải vấn đề chính. Lỗi lớn nằm sau bước sinh test: verify test sinh ra bằng Maven/Gradle, JaCoCo và PIT.
- So với mốc AgoneTest 2025 GPT-4o-mini zero-shot trong proposal, pilot GPT thấp hơn ở build success, branch median và mutation median.
- EvoSuite ổn định hơn về end-to-end success: 28/60 class có metric, trong khi GPT chỉ 7/60.
- GPT có mutation compiled-only mean cao hơn EvoSuite trên các test đã compile thành công, nhưng đây là mẫu rất nhỏ và bị selection bias; strict-zero-fill cho thấy EvoSuite vẫn cao hơn theo mean ở tất cả metric.

## 2. Nguồn tham chiếu và cách đọc so sánh

### 2.1 Nguồn

- Proposal nội bộ: [`proposal.md`](../../../proposal.md). Proposal đã khóa RQ, threshold và decision rule trước khi chạy experiment.
- Paper nguồn: **LLMs for Automated Unit Test Generation and Assessment in Java: The AgoneTest Framework**, arXiv `2511.20403`, bản HTML/PDF: <https://arxiv.org/abs/2511.20403> và <https://arxiv.org/html/2511.20403v2>.
- Bản ACM/ASE 2025 theo DOI được search thấy: <https://dl.acm.org/doi/pdf/10.1109/ASE63991.2025.00198>.
- Paper bối cảnh EvoSuite trong proposal: **Test Wars: A Comparative Study of SBST, Symbolic Execution, and LLM-Based Approaches to Unit Test Generation**, arXiv `2501.10200`: <https://arxiv.org/abs/2501.10200>.
- Paper AgoneTest Workshop/ICSTW 2024/2025 trong proposal: **A System for Automated Unit Test Generation Using Large Language Models and Assessment of Generated Test Suites**, arXiv `2408.07846`: <https://arxiv.org/abs/2408.07846> và bản HTML <https://arxiv.org/html/2408.07846v2>.
- Benchmark nguồn trong proposal: CLASSES2TEST, `147,473` class-level instances từ `9,410` repositories; URL published: <https://anonymous.4open.science/r/classes2test>.

### 2.2 Giá trị tham khảo từ paper/proposal

Bảng dưới đặt các mốc paper bên cạnh kết quả pilot. Mốc chính của đề tài là dòng **AgoneTest 2025 GPT-4o-mini zero-shot**; các dòng few-shot/human chỉ để nhìn bối cảnh, không phải threshold RQ đã khóa.

| Nguồn/mốc | Build | Branch | Line | Method | Mutation | Ghi chú |
| --- | --- | --- | --- | --- | --- | --- |
| AgoneTest 2025 GPT-4o-mini zero-shot | 28.60% | 41.90% | 64.80% | 77.20% | 44.50% | cùng paper AgoneTest 2025; proposal dùng zero-shot làm mốc RQ |
| AgoneTest 2025 GPT-4o-mini few-shot | 25.30% | 62.10% | 71.30% | 81.10% | 61.00% | chỉ để đặt bối cảnh, không phải mốc RQ chính |
| AgoneTest 2025 human-written tests | 100.00% | 48.70% | 73.20% | 74.00% | 40.40% | chỉ để đặt bối cảnh, không phải mốc RQ chính |
| Pilot 60 GPT-4o-mini zero-shot, compiled-only mean | 11.67% | 40.77% | 53.93% | 69.17% | 28.63% | kết quả pilot hiện tại; n compiled GPT = 7 |
| Pilot 60 EvoSuite, compiled-only mean | 46.67% | 46.84% | 55.01% | 71.83% | 12.91% | baseline mở rộng của đề tài; AgoneTest 2025 không công bố EvoSuite trên CLASSES2TEST |

Lưu ý về denominator: proposal khóa cách đọc `AgoneTest-compatible` cho RQ1-RQ3, tức build success tính trên toàn sample, còn branch/line/method/mutation đọc trên các row compile thành công. Báo cáo này không đổi threshold sau khi thấy dữ liệu pilot.


### 2.3 So sánh thêm với AgoneTest Workshop 2024/ICSTW

AgoneTest Workshop 2024/ICSTW sử dụng subset nhỏ hơn: `10` repository, `94` focal classes, tổng `189,703` dòng code và cyclomatic complexity `85,811`. Paper này dùng `gpt-4` và `gpt-3.5-turbo`, không phải `gpt-4o-mini`; vì vậy đây là mốc bối cảnh, **không phải threshold RQ chính** của đề tài.

| Nguồn/mốc Workshop | Build | Pass/green suite | Branch | Line | Method | Mutation | Đặt cạnh pilot này |
| --- | --- | --- | --- | --- | --- | --- | --- |
| AgoneTest Workshop `gpt-4 zero-shot` | `76/94 = 80.85%` | `29/94 = 30.85%` | `77.69%` | `86.64%` | `85.45%` | `54.64%` | Cao hơn pilot GPT ở mọi metric; khác model và subset nhỏ. |
| AgoneTest Workshop `gpt-4 few-shot` | `76/94 = 80.85%` | `28/94 = 29.78%` | `77.56%` | `78.17%` | `83.69%` | `46.17%` | Vẫn cao hơn pilot GPT; zero-shot GPT-4 tốt hơn few-shot trong paper này. |
| AgoneTest Workshop `gpt-3.5-turbo zero-shot` | `64/94 = 68.08%` | `36/94 = 38.29%` | `70.64%` | `77.69%` | `84.82%` | `54.69%` | Cao hơn pilot GPT; nhấn mạnh pilot đang bị nghẽn build/verify mạnh. |
| AgoneTest Workshop `gpt-3.5-turbo few-shot` | `65/94 = 69.14%` | `35/94 = 37.23%` | `68.11%` | `77.56%` | `83.22%` | `45.56%` | Cao hơn pilot GPT, nhưng không cùng model/protocol. |
| AgoneTest Workshop human-written | `94/94 = 100%` | `94/94 = 100%` | `80.89%` | `76.64%` | `69.81%` | `69.06%` | Human mutation cao hơn nhiều; dùng làm bối cảnh chất lượng test người viết. |
| Pilot 60 GPT-4o-mini zero-shot | `7/60 = 11.67%` | không tách green suite riêng | `40.77%` mean compiled-only | `53.93%` mean compiled-only | `69.17%` mean compiled-only | `28.63%` mean compiled-only | Kết quả thực nghiệm hiện tại, n compiled GPT chỉ `7`. |
| Pilot 60 EvoSuite | `28/60 = 46.67%` | không tách green suite riêng | `46.84%` mean compiled-only | `55.01%` mean compiled-only | `71.83%` mean compiled-only | `12.91%` mean compiled-only | Baseline mở rộng của đề tài; không có trong Workshop. |

Cách đọc: Workshop 2024 có build/pass rate cao hơn nhiều, nhưng dùng sample nhỏ và model khác. Pilot hiện tại dùng `gpt-4o-mini-2024-07-18` theo proposal, chạy trên sample 60 đại diện cho full 300, và áp dụng strict logging cho mọi lỗi build/metric. Vì vậy, Workshop hữu ích để đặt bối cảnh “LLM có thể đạt mức cao hơn trong điều kiện thuận lợi”, nhưng không được dùng để thay ngưỡng `44.5 / 41.9 / 28.6` đã khóa từ AgoneTest 2025.

### 2.4 Các mốc bối cảnh khác trong proposal

Các mốc dưới đây giúp đọc trực quan vị trí của pilot, nhưng **không** được dùng làm threshold quyết định RQ chính vì khác benchmark, khác model hoặc khác protocol.

| Nguồn trong proposal | Benchmark/protocol | Mốc được nhắc trong proposal | Đặt cạnh pilot này | Cách đọc |
| --- | --- | --- | --- | --- |
| Test Wars 2025 | GitBug Java, không phải CLASSES2TEST | EvoSuite best average mutation `30.56%` | Pilot EvoSuite mutation compiled-only mean `12.91%`, strict mean `6.02%` | Chỉ là bối cảnh cho EvoSuite; không so sánh trực tiếp vì dataset/protocol khác. |
| MutGen 2026 | HumanEval-Java + LeetCode-Java, synthetic/benchmark lập trình | Mutation `89.5% / 89.1%` | Không đặt cạnh số pilot làm threshold | Không cùng real-world repo benchmark; chỉ củng cố lý do chọn mutation làm metric chính. |


## 3. Thông tin run và dataset pilot

- Run ID: `20260717_084920_5f360ff0`
- Mode: `full_run`
- Sample: `D:\balanced_delivery\balanced_bundle_300\output\pilot_60_classes.csv`
- Model GPT: `gpt-4o-mini-2024-07-18`
- Prompt: `rbl4-zero-shot`
- Thời gian: bắt đầu `2026-07-17T01:49:20+00:00`, hoàn tất `2026-07-17T03:23:04+00:00`, tổng khoảng `93.7` phút.
- Class đầu vào: 60; buildable run: 60; skipped: 0.
- Repo được chạy: 33; module baseline: 41.
- Build tool trong 60 class: Maven=54, Gradle=6.
- Java version metadata: {'8': 28, 'unknown_but_builds_with_jdk8': 22, '6': 5, '7': 5}.

### 3.1 Độ phức tạp class trong pilot

| Metric | Mean | Median | Min | Max |
| --- | --- | --- | --- | --- |
| NLOC | 165.27 | 123.50 | 9.00 | 471.00 |
| Max_Method_CC | 7.53 | 7.00 | 2.00 | 14.00 |
| Public_Method_Count | 13.00 | 8.50 | 1.00 | 56.00 |
| Method_Count | 17.25 | 13.00 | 1.00 | 66.00 |
| Avg_Method_CC | 2.30 | 2.16 | 1.11 | 6.20 |
| Sum_Method_CC | 37.10 | 30.00 | 3.00 | 100.00 |

### 3.2 Cấu trúc thư mục RBL-4 và luồng dữ liệu

Trong container, root logic của thí nghiệm là `/pilot`. Trên máy Windows hiện tại, `/pilot` tương ứng với `D:\balanced_delivery\balanced_bundle_300`. Báo cáo này đọc mọi đường dẫn theo mapping đó.

```text
/pilot
|-- class_sampling_manifest_seed42.csv
|-- repos/<repo_id>/
|-- compiledrepos -> repos
|-- repair_configs/<repo_id>/
|-- metadata/
|-- output/
|-- results/runs/<run_id>/
|-- AgoneTest/
|-- experiment_tool/
|-- scripts/
|-- run_rbl4_part1_buildable_experiment.py
```

| Nhánh thư mục/file | Vai trò trong RBL-4 | Liên hệ với run pilot này |
| --- | --- | --- |
| `class_sampling_manifest_seed42.csv` | Manifest gốc: 300 class main và 58 backup, giữ `repo_id`, focal class, test path, complexity, metadata chọn mẫu | Nguồn ban đầu để tạo pilot 60 và full 300. |
| `repos/<repo_id>/` | Mã nguồn gốc của 33 repository | Baseline build và generated-test verification đều chạy trên mã nguồn này. |
| `compiledrepos` | Alias/symlink tương thích với AgoneTest cũ | Trỏ về `repos`, không phải bản source thứ hai. Các path staged có thể dùng alias này. |
| `repair_configs/<repo_id>/` | Cấu hình sửa môi trường/build tối thiểu theo repo | Ví dụ `37728390/local_maven_repair.json`, `58767125/local_gradle_repair.json`; dùng để baseline sạch mà không sửa source chính. |
| `metadata/build_recipes.jsonl` | Build recipe đã chuẩn hóa cho từng repo/module | Runner dùng để chọn working directory, build tool, Java version, command Maven/Gradle. |
| `metadata/pilot_60_selection_report.json` | Audit cách tách pilot 60 | Xác nhận pilot có 60 row, phủ 33 repo, không duplicate focal/test row. |
| `output/classes_main.csv` | Tập full 300 class main | Đây là input nên dùng cho full 300 sau khi pilot ổn. |
| `output/classes_backup.csv` | Tập backup 58 class | Dùng thay thế nếu một class main bị loại theo rule đã khóa. |
| `output/pilot_60_classes.csv` | Tập pilot 60 class | Run `20260717_084920_5f360ff0` dùng file này làm `source_sample_csv`. |
| `output/pilot_60_backups.csv` | Backup riêng cho pilot | Không được tính vào metric pilot trừ khi có quy trình thay thế rõ ràng. |
| `output/classes_part1.csv`, `classes_part2.csv`, `classes_part3.csv` | Chia full 300 thành các batch nhỏ | Hữu ích nếu chạy full 300 theo từng phần để tránh mất kiểm soát runtime. |
| `output/<repo_id>/` | Output sống do AgoneTest ghi theo project | Có thể bị ghi đè/cập nhật trong lúc chạy; dùng để debug raw project output. |
| `results/runs/<run_id>/` | Snapshot chuẩn của một lần chạy | Đây là nguồn audit chính cho báo cáo: metric, lỗi, generated tests, manifest, logs. |
| `AgoneTest/` | Framework sinh/chạy test: EvoSuite, GPT, JaCoCo, PIT, Maven/Gradle helpers | RBL-4 gọi lại các module này thay vì viết lại toàn bộ logic đo lường. |
| `experiment_tool/` | Backend/UI orchestration | Tạo run, ghi status, phục vụ giao diện theo dõi. |
| `scripts/create_rbl4_samples.py` | Script tách main/backup/pilot/batch | Sinh các file trong `output/` từ manifest gốc. |
| `run_rbl4_part1_buildable_experiment.py` | Runner chính cho RBL-4 part 1 buildable-only | Tạo run folder, chạy precheck, baseline, generation, metrics, summary. |

Luồng dữ liệu đúng để đọc báo cáo là: `class_sampling_manifest_seed42.csv` -> `scripts/create_rbl4_samples.py` -> `output/pilot_60_classes.csv` hoặc `output/classes_main.csv` -> `run_rbl4_part1_buildable_experiment.py` -> `repos/` + `metadata/build_recipes.jsonl` + `repair_configs/` -> `output/<repo_id>/` trong lúc chạy -> `results/runs/<run_id>/` làm snapshot cuối. Vì vậy, khi có khác biệt giữa `output/` và `results/runs/<run_id>/`, báo cáo ưu tiên `results/runs/<run_id>/` vì nó là artifact đã đóng gói theo run.

## 4. Precheck, baseline build và môi trường

- Precheck: `PASS`, buildable rows 60, skipped rows 0.
- Baseline build: {'PASS': 41}; tổng thời gian baseline module khoảng 382.0s, module chậm nhất 26.9s.
- Baseline failed repo: không có. Vì vậy các lỗi phía sau không nên đọc là repo gốc không build được.

Local repair/preinstall được áp dụng trong baseline:
| Project | Module | Focal classes | Prepare status | Prepare detail |
| --- | --- | --- | --- | --- |
| 37728390 | contrib/geomason | AttributeValue | PASS | requested=1.8 selected_jdk=8 java_home=C:\Program Files\Java\jdk1.8.0_172; Local Maven repair completed |

- Runtime errors: {'OK': 33}; tất cả 33 project ghi `OK`.
- API GPT: 59/59 call OK, 0 API error; tổng token 177,165 = prompt 132,522 + completion 44,643; tổng latency API 728.0s, mean 12.34s/call.
- GPT có 59 API call OK cho 60 class; 1 class thiếu output là `58767125 / DefaultBigDecimalMath`, không phải lỗi OpenAI API.

## 5. So sánh trực quan: pilot GPT với mốc AgoneTest 2025

| Chỉ số | Pilot GPT | AgoneTest 2025 GPT-4o-mini zero-shot | Chênh lệch pilot - mốc | Đọc nhanh |
| --- | --- | --- | --- | --- |
| Compilation/build success GPT | 7/60 = 11.67% | 28.60% | -16.93 pp | dưới mốc rõ rệt |
| Branch coverage GPT compiled-only | mean 40.77%, median 35.71%, n=7 | 41.90% | mean -1.13 pp, median -6.19 pp | dưới mốc |
| Line coverage GPT compiled-only | mean 53.93%, median 54.17%, n=7 | 64.80% | mean -10.87 pp, median -10.63 pp | dưới mốc |
| Method coverage GPT compiled-only | mean 69.17%, median 74.42%, n=7 | 77.20% | mean -8.03 pp, median -2.78 pp | dưới mốc |
| Mutation score GPT compiled-only | mean 28.63%, median 30.00%, n=7 | 44.50% | mean -15.87 pp, median -14.50 pp | dưới mốc |

Diễn giải nhanh: nếu pilot đạt tỷ lệ build của AgoneTest 2025 (28.6%), ta kỳ vọng khoảng 17.2 test GPT compile thành công trên 60 class. Thực tế chỉ có 7 test. Đây là bottleneck lớn nhất trước khi nhìn chất lượng coverage/mutation.

## 6. Kết quả theo RQ trong proposal

| RQ | Metric/mốc proposal | Pilot quan sát | So với mốc | Diễn giải pilot |
| --- | --- | --- | --- | --- |
| RQ1 | GPT mutation compiled-only median >= 44.5% | median 30.00%, mean 28.63%, n compiled=7 | -14.50 pp | Chỉ mô tả vì n compiled < 60; hiện dưới mốc. |
| RQ2 | GPT branch compiled-only median >= 41.9% | median 35.71%, mean 40.77%, n compiled=7 | -6.19 pp | Chỉ mô tả vì n compiled < 60; mean gần mốc nhưng median dưới mốc. |
| RQ3 | GPT build success >= 28.6% | 7/60 = 11.67%; p-greater=0.999536 | -16.93 pp | Không có tín hiệu đạt mốc 28.6%; pilot thấp hơn kỳ vọng khoảng 10 class. |
| RQ4 | Median GPT - EvoSuite strict mutation >= -5 pp | median diff 0.00 pp; mean strict diff -2.68 pp; p=0.000472; A12=0.4 | median đạt margin, mean thấp hơn EvoSuite 2.68 pp | Có tín hiệu non-inferiority theo median do nhiều tie zero, nhưng A12=0.4 cho thấy GPT không trội hơn EvoSuite. |

Ghi chú: RQ1 và RQ2 trong proposal chỉ được diễn giải confirmatory nếu số GPT compiled-success rows đủ lớn. Pilot hiện có 7 row compiled, thấp hơn ngưỡng fallback `< 60`, nên chỉ nên dùng để phát hiện vấn đề và ước lượng rủi ro trước full 300.

## 7. GPT so với EvoSuite trong pilot

### 7.1 Compiled-only

| Arm | Compile success | Branch mean/median | Line mean/median | Method mean/median | Mutation mean/median |
| --- | --- | --- | --- | --- | --- |
| GPT | 7/60 = 11.67% | 40.77% / 35.71% | 53.93% / 54.17% | 69.17% / 74.42% | 28.63% / 30.00% |
| EvoSuite | 28/60 = 46.67% | 46.84% / 51.73% | 55.01% / 55.84% | 71.83% / 84.52% | 12.91% / 7.18% |

Compiled-only cho thấy GPT có mutation mean/median cao hơn EvoSuite trên 7 class hiếm hoi compile được. Nhưng do GPT chỉ compile 7/60, bảng này phải đọc kèm strict-zero-fill ở dưới.

### 7.2 Strict-zero-fill end-to-end

| Metric strict-zero-fill | GPT mean | EvoSuite mean | GPT - EvoSuite mean | GPT - EvoSuite median | Đọc nhanh |
| --- | --- | --- | --- | --- | --- |
| Branch | 4.76% | 21.86% | -17.10 pp | 0.00 pp | EvoSuite cao hơn theo mean |
| Line | 6.29% | 25.67% | -19.38 pp | 0.00 pp | EvoSuite cao hơn theo mean |
| Method | 8.07% | 33.52% | -25.45 pp | 0.00 pp | EvoSuite cao hơn theo mean |
| Mutation | 3.34% | 6.02% | -2.68 pp | 0.00 pp | EvoSuite cao hơn theo mean |

Strict-zero-fill phạt mọi lỗi generation/build/metric bằng 0. Theo mean, EvoSuite cao hơn GPT ở cả branch, line, method và mutation. Riêng median chênh mutation bằng 0 vì có rất nhiều class cả hai arm đều fail hoặc cùng 0, nên RQ4 non-inferiority có thể nhìn tích cực hơn mean.

### 7.3 Ma trận compile giữa hai arm

| Trường hợp | Số class | Tỷ lệ |
| --- | --- | --- |
| Cả hai arm fail metric | 32 | 53.33% |
| Chỉ EvoSuite có metric | 21 | 35.00% |
| Chỉ GPT có metric | 0 | 0.00% |
| Cả hai arm đều có metric | 7 | 11.67% |

Trong 7 class mà cả GPT và EvoSuite đều compile, mutation GPT - EvoSuite có mean +14.82 pp, median +3.03 pp, min -8.11 pp, max +56.25 pp. Branch GPT - EvoSuite trên cùng 7 class có mean -35.94 pp, median -30.00 pp.

### 7.4 Top case có mutation cao

| Arm | Project | Module | Focal class | Branch | Line | Method | Mutation |
| --- | --- | --- | --- | --- | --- | --- | --- |
| evosuite | 88231534 | opentracing-mongo-common | TracingCommandListener | 87.50% | 92.00% | 100.00% | 50.00% |
| evosuite | 54620819 | mvn-golang-wrapper | PackageList | 100.00% | 100.00% | 100.00% | 40.54% |
| evosuite | 74458764 | session-replacement | RedisConfiguration | 90.62% | 93.33% | 100.00% | 39.39% |
| evosuite | 88231534 | opentracing-mongo-common | NoopSpanNameProvider | 100.00% | 100.00% | 100.00% | 33.33% |
| evosuite | 6489406 | ais-lib-communication | Track | 56.67% | 86.98% | 93.85% | 33.33% |
| gpt | 39770930 | proxy | JsonWireUtils | 75.00% | 81.25% | 100.00% | 62.50% |
| gpt | 89562636 | coap-core | CoapTcpCSM | 48.08% | 70.37% | 92.86% | 45.00% |
| gpt | 74458764 | session-replacement | RedisConfiguration | 25.00% | 54.17% | 66.67% | 42.42% |
| gpt | 74458764 | session-replacement | SessionConfiguration | 35.71% | 51.93% | 59.38% | 30.00% |
| gpt | 89562636 | coap-core | CoapPacket | 33.62% | 49.78% | 74.42% | 20.51% |

## 8. Lỗi trong suốt quá trình chạy

### 8.1 Fail/Error theo phase

| Phase | Status | N |
| --- | --- | --- |
| maven_verify_jacoco_pit | FAIL | 60 |
| gradle_test_jacoco_pitest | FAIL | 9 |
| evosuite_generate | FAIL | 2 |

Diễn giải: `maven_verify_jacoco_pit` và `gradle_test_jacoco_pitest` là bước sau khi test được sinh/staged vào repo để chạy verify, coverage và mutation. Fail ở đây chủ yếu là generated test không compile, test fail, hoặc plugin JaCoCo/PIT/assembly fail trong context có test sinh ra.

### 8.2 Nhóm lỗi

| Nhóm lỗi | Mức | N | Repo liên quan |
| --- | --- | --- | --- |
| project_compilation_error | error | 39 | 3526892, 37489990, 38080174, 49126308, 50552134, 5093728, 5175291, 54620819, 56342003, 6489406 ... |
| test_framework_dependency_missing | error | 11 | 5175291, 54231507, 79246257, 82282677 |
| unclassified_runtime_issue | warning | 10 | 37728390, 5093728, 56342003, 58086354, 70711909, 75368166, 960343 |
| gradle_build_failed | error | 9 | 57251243, 5745625, 58086354, 7283919 |
| generated_test_assertion_failure | error | 1 | 37728390 |
| java_source_level_incompatible | error | 1 | 5093728 |

Nhận xét theo category:

- `project_compilation_error` là nhóm lớn nhất: generated test tham chiếu sai symbol/constructor/API, gọi method private, type mismatch, hoặc code test không hợp với source hiện tại.
- `test_framework_dependency_missing`: generated test dùng Mockito/framework/import không có trong module test classpath, hoặc source level cũ làm dependency/test framework không khớp.
- `gradle_build_failed`: tập trung ở các module Gradle khi gắn JaCoCo/PIT/test task sau generation.
- `unclassified_runtime_issue`: lỗi plugin/assembly/JaCoCo/test runtime không rơi vào category compile rõ ràng; cần mở `phase_log.csv` nếu muốn sửa từng repo.
- `generated_test_assertion_failure`: test compile nhưng assertion fail; gặp ở `37728390/contrib/geomason`.
- `java_source_level_incompatible`: test sinh ra hoặc plugin path không hợp source level cũ; gặp ở `5093728/core`.

### 8.3 Hotspot repo/phase

| Project | Phase | Fail events |
| --- | --- | --- |
| 38080174 | maven_verify_jacoco_pit | 8 |
| 56342003 | maven_verify_jacoco_pit | 6 |
| 5093728 | maven_verify_jacoco_pit | 4 |
| 5175291 | maven_verify_jacoco_pit | 4 |
| 79246257 | maven_verify_jacoco_pit | 4 |
| 70711909 | maven_verify_jacoco_pit | 3 |
| 6489406 | maven_verify_jacoco_pit | 3 |
| 7283919 | gradle_test_jacoco_pitest | 3 |
| 58086354 | gradle_test_jacoco_pitest | 3 |
| 89562636 | maven_verify_jacoco_pit | 2 |
| 57251243 | gradle_test_jacoco_pitest | 2 |
| 54231507 | maven_verify_jacoco_pit | 2 |

### 8.4 Fail stage trong `metrics_long.csv`

| Arm | Fail stage | N |
| --- | --- | --- |
| evosuite | compilation_or_metric_failure | 1 |
| evosuite | generated_test_no_successful_build_or_metrics | 19 |
| evosuite | generation_or_build_failed_marker | 11 |
| evosuite | missing_output | 1 |
| gpt | compilation_or_metric_failure | 3 |
| gpt | generated_test_no_successful_build_or_metrics | 49 |
| gpt | missing_output | 1 |

### 8.5 Missing output

| Row | Project | Module | Focal class | Arm |
| --- | --- | --- | --- | --- |
| 58 | 58767125 | ch.obermuhlner.math.big | DefaultBigDecimalMath | gpt |
| 58 | 58767125 | ch.obermuhlner.math.big | DefaultBigDecimalMath | evosuite |

### 8.6 EvoSuite generation failures

| Thời điểm UTC | Project | Focal class | Duration | Detail |
| --- | --- | --- | --- | --- |
| 2026-07-17T02:40:10+00:00 | 58086354 | ClientInterceptors | 0.001 | missing build/classes/java/main |
| 2026-07-17T02:48:11+00:00 | 70711909 | HereAccount | 12.736 | - C:\Users\admin\.m2\repository\org\apache\commons\commons-configuration2\2.12.0\commons-configuration2-2.12.0.jar<br>  - C:\Users\admin\.m2\repository\org\apache\commons\commons-lang3\3.18.0\commons-lan... |

## 9. Giải thích một số lỗi đáng chú ý

- `58767125 / DefaultBigDecimalMath`: cả GPT và EvoSuite đều `missing_output`, không có generated test file trong manifest. Đây là case nên ưu tiên kiểm tra trước full 300 vì mất cả hai arm.
- `58086354 / ClientInterceptors`: EvoSuite fail gần như tức thời với `missing build/classes/java/main`. Nghĩa là Gradle module không để class output ở vị trí tool đang kỳ vọng, dù baseline testClasses có thể pass.
- `70711909 / HereAccount`: EvoSuite generation fail sau khi đọc classpath; run này không còn lỗi `.tar.gz` fatal như trước, nhưng vẫn cần mở raw detail nếu muốn sửa riêng classpath/EvoSuite cho repo này.
- `960343 / SimpleTier`: EvoSuite sinh được test nhưng Maven verify fail ở assembly descriptor `src/main/assembly/packaging.xml`; GPT fail thêm ở `testCompile cannot find symbol`. Đây là lỗi verify sau generation, không phải baseline clean build fail.
- `37728390 / AttributeValue`: local Maven repair đã giúp baseline pass; sau generation có test assertion failure. Đây là lỗi behavior của generated test, không phải cache `jai_core`.
- `50552134`, `38080174`, `6489406`, `79246257`, `812511`, `88231534`: nhiều lỗi compile nằm ở generated/original staged test path trong `src/test/java`; cần đọc file `.mavenfailed` nếu muốn sửa theo repo.

## 10. Giải thích chi tiết các file trong folder run

Folder `results/runs/20260717_084920_5f360ff0` là snapshot đầy đủ của một lần chạy pilot trong cấu trúc RBL-4. Nó khác với `output/<repo_id>/`: `output/` là workspace sống của AgoneTest theo project, còn run folder là bằng chứng đã đóng gói để audit và viết báo cáo.

| File/thư mục | Vai trò | Thông tin chính chứa trong file | Dùng trong báo cáo |
| --- | --- | --- | --- |
| `backups/` | Bản sao một số artifact trung gian trước/sau khi runner gom kết quả | File backup phục vụ khôi phục hoặc đối chiếu khi cần debug | Không dùng làm nguồn metric chính nếu file CSV/JSON top-level đã có. |
| `generated_tests/` | Thư mục chứa generated test `.java` đã snapshot theo run | Test do GPT/EvoSuite sinh ra, được tách khỏi workspace sống | Dùng để kiểm tra thủ công test compile fail, assertion fail, dependency mismatch. |
| `status.json` | Trạng thái do UI/backend ghi trong lúc run | `run_id`, `status`, `created_at`, `started_at`, `pid`, sample, model, prompt | Phát hiện trạng thái stale: file còn `running` dù process đã hết. |
| `manifest.json` | Manifest cuối run do runner ghi | `status=completed`, thời điểm hoàn tất, source sample, số class, số repo, fairness policy, danh sách repo chạy | Nguồn chính để kết luận run đã hoàn tất. |
| `environment_checks.csv` | Kiểm tra môi trường trước khi chạy | API key, OpenAI/httpx, JDK phát hiện được, Maven/Gradle/tooling | Xác nhận môi trường đủ điều kiện chạy. |
| `staged_classes.csv` | 60 class sau precheck file path | Metadata class, repo, module, đường dẫn focal/test đã map sang `compiledrepos` | Xác nhận sample được stage đúng. |
| `skipped_classes.csv` | Các class bị loại ở precheck | Rỗng trong run này, có header và `skip_reason` | Xác nhận `precheck_skipped_n=0`. |
| `baseline_build.csv` | Build/test-compile sạch trước khi sinh test | 41 module, command Maven/Gradle, status, duration, Java toolchain, local repair | Nguồn kết luận baseline 41/41 PASS. |
| `generation_classes.csv` | Class được đưa sang generation sau baseline | 60 class với baseline status/detail gắn vào từng row | Nguồn nối sample -> generation. |
| `project_info.json` | Metadata build theo project/module | Build tool, Java version, module/recipe info | Hỗ trợ runner chọn Maven/Gradle/JDK. |
| `api_log.csv` | Log gọi GPT API | model requested/returned, duration, prompt/completion chars, tokens, status/error | Nguồn số `59/59` API OK, token và latency. |
| `phase_log.csv` | Timeline chi tiết theo phase | precheck, baseline_build, evosuite_generate, maven/gradle verify, report; status PASS/FAIL/START | Nguồn đếm lỗi theo phase và hotspot repo. |
| `runtime_errors.csv` | Lỗi runtime cấp project khi gọi AgoneTest | `OK` hoặc exception/traceback theo project | Xác nhận 33 project không crash cấp runner. |
| `metrics_long.csv` | Bảng metric dài, 1 row/class/arm | compilation, branch/line/method/mutation compiled-only, strict-zero-fill, fail_stage | Nguồn chính cho mọi metric GPT/EvoSuite. |
| `summary.csv` | Tổng hợp theo arm | compiled_success_n/rate, mean/median metric, p-value RQ, A12 | Nguồn bảng summary/RQ nhanh. |
| `generated_failures.csv` | Subset các row không thành công | generated file, failure markers, fail_stage, metric zero-fill | Nguồn phân tích fail_stage. |
| `error_summary.csv` | Phân loại lỗi đã chuẩn hóa | category, severity, explanation_vi, suggested_action_vi, detail | Nguồn nhóm lỗi như `project_compilation_error`, `gradle_build_failed`. |
| `generated_tests_manifest.csv` | Manifest file test sinh ra đã copy vào run | project, arm, file_name, source_path, stored_path, size, modified_at | Nguồn đếm GPT=59, EvoSuite=48 generated files. |
| `generated_tests.zip` | Gói nén generated tests | Bản nén các `.java` test sinh ra | Artifact để chia sẻ/đối chiếu code test. |
| `experiment_report.xlsx` | Báo cáo Excel | Các sheet summary, metrics, failures, errors, generated tests | Artifact phục vụ đọc bằng Excel. |
| `stdout.log` | Log stdout thô | Output Maven/Gradle/EvoSuite/GPT runner | Dùng khi cần điều tra lỗi sâu hơn. |
| `stderr.log` | Log stderr thô | Java version output, warning/error stderr | Dùng khi cần kiểm tra lỗi môi trường/toolchain. |
| `PILOT60_ANALYSIS.md` | Báo cáo phân tích hiện tại | Diễn giải số liệu, so sánh paper, RQ, lỗi, khuyến nghị | File bạn đang đọc. |

## 11. Số liệu trong báo cáo được trích từ đâu

Bảng này là “data lineage” để audit lại các con số trong `PILOT60_ANALYSIS.md`.

| Số liệu/nhận định trong báo cáo | Nguồn trực tiếp | Cột/trường liên quan | Cách tính/diễn giải |
| --- | --- | --- | --- |
| Tách pilot 60 từ full 300 | `class_sampling_manifest_seed42.csv`, `metadata/pilot_60_selection_report.json`, `output/pilot_60_classes.csv` | `main_rows=300`, `backup_rows=58`, `pilot_rows=60`, `pilot_repositories=33` | Script sampling tạo pilot từ manifest gốc; run pilot dùng đúng `output/pilot_60_classes.csv`. |
| Mapping `/pilot` trong container sang Windows | `manifest.json`, cấu trúc thư mục root | `source_sample_csv`, đường dẫn `D:\balanced_delivery\balanced_bundle_300\...` | Trong container đọc là `/pilot/...`; trên host Windows là root bundle hiện tại. |
| Run hoàn tất nhưng `status.json` stale | `manifest.json`, `status.json`, kiểm tra process Windows | `manifest.status=completed`, `status.status=running`, `pid=17148` | Ưu tiên `manifest.json` vì được ghi cuối run; process không còn nên `status.json` stale. |
| 60/60 class buildable, 0 skipped | `manifest.json`, `skipped_classes.csv`, `staged_classes.csv` | `source_sample_n`, `buildable_run_n`, `precheck_skipped_n`; số row CSV | Kiểm tra staged có 60 row và skipped có 0 row. |
| 33 repo, 41 module baseline | `manifest.json`, `baseline_build.csv` | `projects_run`, số row baseline | 33 repo trong manifest; baseline theo module nên có 41 row. |
| Baseline 41/41 PASS | `baseline_build.csv` | `status` | Đếm `status == PASS`. |
| Local repair 37728390 | `baseline_build.csv` | `prepare_status`, `prepare_detail` | Row có `prepare_status=PASS`, detail `Local Maven repair completed`. |
| 59/59 API OK, token/latency | `api_log.csv` | `status`, `prompt_tokens`, `completion_tokens`, `total_tokens`, `duration_sec` | Đếm status OK và cộng token/duration. |
| GPT compile success 7/60 | `summary.csv`, `metrics_long.csv` | `compiled_success_n`, `compiled_success_rate`, `compilation` | `sum(compilation)` trên arm GPT hoặc đọc summary. |
| EvoSuite compile success 28/60 | `summary.csv`, `metrics_long.csv` | tương tự | `sum(compilation)` trên arm EvoSuite. |
| Branch/line/method/mutation compiled-only | `metrics_long.csv`, `summary.csv` | `*_compiled_only` | Mean/median trên các row có `compilation=1`. |
| Strict-zero-fill metrics | `metrics_long.csv`, `summary.csv` | `*_strict_zero_fill` | Fail generation/build/metric được tính 0. |
| Ma trận compile hai arm | `metrics_long.csv` | `source_row_index`, `arm`, `compilation` | Pivot theo class và arm; đếm cả hai fail/chỉ EvoSuite/cả hai pass. |
| Fail stage | `metrics_long.csv`, `generated_failures.csv` | `fail_stage` | Groupby `arm, fail_stage`. |
| Lỗi theo phase | `phase_log.csv` | `phase`, `status` | Lọc `FAIL/ERROR`, groupby phase/status. |
| Nhóm lỗi chuẩn hóa | `error_summary.csv` | `category`, `severity`, `project`, `detail` | Groupby category/severity. |
| Missing output 58767125 | `metrics_long.csv`, `generated_tests_manifest.csv` | `fail_stage=missing_output`, `generated_test_file_exists=0` | Cả arm GPT và EvoSuite không có generated file. |
| Số file generated tests | `generated_tests_manifest.csv` | `arm`, `file_name`, `size_bytes` | Đếm file theo arm. |
| Mốc AgoneTest 2025 | `proposal.md`, arXiv `2511.20403` Table IV/V | Build, branch, line, method, mutation | Dùng làm threshold chính theo proposal. |
| Mốc AgoneTest Workshop | arXiv `2408.07846` Table IV/V | Build/pass và coverage/mutation | Dùng làm bối cảnh, không làm threshold RQ. |
| Mốc Test Wars/MutGen | `proposal.md`, arXiv nguồn liên quan | mutation/context | Dùng làm bối cảnh khác benchmark. |

## 12. Phương thức kết luận 4 Research Questions

Phần này mô tả đúng phương pháp trong `proposal.md`; pilot chỉ là phân tích mô tả nếu chưa đủ điều kiện confirmatory.

| RQ | Dữ liệu đầu vào | Phương pháp thống kê/decision rule | Điều kiện kết luận confirmatory | Kết luận pilot 60 |
| --- | --- | --- | --- | --- |
| RQ1 - Mutation replication | GPT rows có `compilation=1`, cột `mutation_score_compiled_only` trong `metrics_long.csv` | One-sample Wilcoxon signed-rank, one-tailed, kiểm tra median mutation có lớn hơn mốc `44.5%`; `alpha=0.05` | Chỉ diễn giải confirmatory nếu GPT compiled-success rows `>= 60` ở full run | Pilot chỉ mô tả: n compiled=7, median `30.00%`, mean `28.63%`, dưới mốc `44.5%`; không kết luận confirmatory. |
| RQ2 - Branch replication | GPT rows có `compilation=1`, cột `branch_coverage_compiled_only` | One-sample Wilcoxon signed-rank, one-tailed, kiểm tra median branch có lớn hơn mốc `41.9%`; `alpha=0.05` | Chỉ diễn giải confirmatory nếu GPT compiled-success rows `>= 60` ở full run | Pilot chỉ mô tả: n compiled=7, median `35.71%`, mean `40.77%`, dưới mốc median `41.9%`; không kết luận confirmatory. |
| RQ3 - Build/compilation success | Toàn bộ 60 GPT rows, cột `compilation` | Exact binomial test, one-tailed `greater`, với `p0=0.286`; reject H0 nếu `p<0.05` và observed proportion `>=0.286` | Có thể áp dụng trên full N=300; pilot dùng để smoke-test xu hướng | Pilot: `7/60=11.67%`, p-greater `0.999536`, thấp hơn mốc `28.6%`; không có tín hiệu đạt RQ3. |
| RQ4 - Non-inferiority GPT vs EvoSuite | Cặp per-class GPT/EvoSuite trên cùng `source_row_index`, cột `mutation_score_strict_zero_fill` | Tính `diff_i = GPT_i - EvoSuite_i`; kiểm tra non-inferiority margin `-5 pp` bằng Wilcoxon one-tailed trên `diff_i + 5`; báo cáo median diff và Vargha-Delaney A12 | Dùng strict whole-sample, zero-fill mọi failure; full N=300 mới là kết luận chính | Pilot: median diff `0.00 pp`, mean diff `-2.68 pp`, p `0.000472`, A12 `0.4`. Có tín hiệu đạt margin theo median, nhưng do nhiều tie zero và A12<0.5, không nên diễn giải là GPT tốt hơn EvoSuite. |

Cách diễn đạt kết luận cuối cùng cho full 300 nên là:

1. **RQ1/RQ2:** nếu compiled rows `<60`, báo cáo descriptive, không dùng p-value để claim confirmatory. Nếu `>=60`, dùng Wilcoxon one-tailed và so sánh observed median với mốc.
2. **RQ3:** dùng exact binomial test trên toàn bộ sample GPT; đây là thước đo usability end-to-end quan trọng nhất cho GPT.
3. **RQ4:** dùng paired strict-zero-fill per class để so GPT với EvoSuite công bằng trên cùng sample; luôn báo cáo cả p-value, median diff, mean diff và A12 để tránh bị median tie zero đánh lừa.

## 13. Artifact được tạo

- `generated_tests/`: thư mục snapshot test `.java` sinh bởi GPT/EvoSuite theo run.
- `backups/`: backup artifact trung gian phục vụ debug/khôi phục.
- `metrics_long.csv`: 120 rows = 60 class x 2 arm.
- `summary.csv`: 2 rows, tổng hợp theo arm GPT/EvoSuite.
- `generated_failures.csv`: 85 failure rows.
- `generated_tests_manifest.csv`: 107 stored generated test files; GPT=59, EvoSuite=48.
- `generated_tests.zip`: archive generated tests, size 107,197 bytes.
- `experiment_report.xlsx`: Excel report, size 113,917 bytes.

## 14. Ước lượng cho full 300

- Theo tỷ lệ GPT compile của pilot (11.67%), full 300 có thể chỉ đạt khoảng 35.0 compiled-success GPT rows nếu không sửa thêm pipeline/repo.
- Nếu đạt mốc paper/proposal 28.6%, full 300 kỳ vọng khoảng 85.8 compiled-success GPT rows. Khoảng cách giữa pilot-rate và paper-rate là khoảng 50.8 row trên 300 class.
- Pilot dùng 177,165 tokens cho 60 class; ngoại suy tuyến tính full 300 khoảng 885,825 tokens.
- Pilot mất khoảng 93.7 phút; ngoại suy thô full 300 khoảng 468.7 phút nếu chạy tuần tự cùng tốc độ. Thực tế có thể lệch vì PIT/Gradle/Maven fail nhanh hoặc treo lâu tùy repo.

## 15. Khuyến nghị trước khi chạy full 300

1. Có thể chạy full 300 vì baseline pilot đã sạch: 0 skipped, 0 baseline failed. Nhưng cần chấp nhận rằng fail generated-test là kết quả hợp lệ của protocol nếu không sửa test sinh ra.
2. Nên kiểm tra riêng `58767125 / DefaultBigDecimalMath` trước full, vì case này mất cả GPT và EvoSuite output.
3. Nên cải thiện Gradle class output/PIT-JaCoCo cho các repo hotspot: `57251243`, `5745625`, `58086354`, `7283919`, nếu mục tiêu là giảm lỗi hạ tầng.
4. Nên sửa sync `status.json` theo `manifest.json`, vì run này completed nhưng status file stale `running`; nếu không, UI có thể làm người dùng tưởng run vẫn đang chạy.
5. Nếu giữ đúng proposal, không nên đổi prompt hoặc auto-repair generated tests sau khi đã thấy pilot. Nếu muốn tăng pass rate bằng repair, nên ghi rõ là amendment hoặc experiment phụ, không trộn với RQ1-RQ3 chính.

## 16. Kết luận

Pilot 60 xác nhận phần quan trọng nhất của môi trường: dataset và repo baseline có thể build sạch trên máy hiện tại. Tuy nhiên, so với mốc AgoneTest 2025 trong proposal, GPT-4o-mini zero-shot đang thấp hơn rõ ở build success và mutation/branch median. EvoSuite có end-to-end success tốt hơn trong pilot, nhưng mutation compiled-only của GPT ở các case thành công vẫn có vài điểm sáng. Trước full 300, quyết định quan trọng là giữ nguyên protocol để đo thất bại thật của generated tests, hay mở một nhánh repair/tooling riêng để giảm lỗi hạ tầng và tăng số compiled-success rows.
