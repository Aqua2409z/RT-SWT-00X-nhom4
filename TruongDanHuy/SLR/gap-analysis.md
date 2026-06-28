# GAP Analysis – LLM cho sinh Unit Test
Bảng bằng chứng: N = 16 | Ngày: 2026-06-08

## Bảng GAP

| Cột | Phát hiện | Loại GAP | Phản chứng |
|-----|-----------|----------|------------|
| Tool/LLM | Không paper nào so sánh từ 2 LLM trở lên một cách độc lập trên cả Java lẫn Python với cùng bộ đối tượng và cùng giao thức đo lường | GAP-T | Kiểm tra 16 paper — xác nhận |
| Dataset | Benchmark phân mảnh (hơn 12 bộ dữ liệu khác nhau qua 16 paper); 4 bộ dữ liệu có nguy cơ rò rỉ dữ liệu (Defects4J, HumanEval-Java, LeetCode-Java, QuixBugs) | GAP-D | Kiểm tra 16 paper — xác nhận |
| Metric | Chỉ 4/16 paper báo cáo điểm mutation (ID 9, 12, 21, 55); ID 9 là paper duy nhất báo cáo cả 4 metric trên cùng bộ dữ liệu (CLASSES2TEST), nhưng (a) mutation chỉ tính trên **green suite** (30–38% lớp test vượt build), không phải toàn bộ test được sinh ra; (b) Java only — chưa có benchmark chuẩn hóa nào so sánh 4 metric này trên Python song song | GAP-M | Kiểm tra 16 paper — xác nhận; xem chi tiết phản chứng ID 9 bên dưới |
| Hạn chế | 15/16 paper giới hạn một ngôn ngữ duy nhất (Java only: ID 3, 5, 9, 11, 12, 13, 15, 16, 21, 24, 31, 74; Python only: ID 1 một phần, 32, 41; C# only: ID 15); 6/16 paper tự nêu trong Threats to Validity (ID 1, 5, 13, 15, 21, 74) | GAP-S | Kiểm tra 16 paper — xác nhận |

## GAP Chính: GAP-M (Metric)

**Luận điểm trung tâm:** Trong 4 paper có báo cáo mutation score, không paper nào đồng thời thỏa mãn ba điều kiện: **(A) mutation là primary outcome của RQ**, **(B) bộ dữ liệu Java real-world** (không phải synthetic), **(C) có baseline SBST để so sánh (EvoSuite)**. Mỗi paper chỉ đạt được tối đa 2/3 điều kiện này.

### Bằng chứng nền: Tại sao cần mutation score?

**ID 55** (MutGen, 2026, TSE) **[p.3, Section II — “Motivation”]** chứng minh thực nghiệm: subject id_81 đạt **100% line và branch coverage** nhưng chỉ **4% mutation score** với vanilla LLM prompting (GENvanilla) — test bao phủ mọi dòng nhưng gần như không phát hiện được lỗi. Dừng lại ở coverage là chưa đủ. **[p.9, Table I — “SUMMARY OF RESULTS”]** GENvanilla: HumanEval-Java mutation score chỉ **77.9%** (so sánh với coverage 96.2%/92.8%) — xác nhận gap giữa coverage và fault detection trên bảng đầy đủ.

### Bảng 3 góc — Tại sao chưa có paper nào lấp đầy

| Paper | (A) Mutation primary? | (B) Real-world dataset? | (C) vs EvoSuite? | Còn thiếu |
|-------|-----------------------|-------------------------|-----------------|------------|
| **ID 9** – AgoneTest (2025, ASE) | phụ + green-suite | CLASSES2TEST | chỉ vs human-written | Góc A + C |
| **ID 12** – AgoneTest Workshop (2024, ICSTW) | phụ + green-suite | CLASSES2TEST | chỉ vs human-written | Góc A + C |
| **ID 21** – Test Wars (2025, ICST) | phụ | GitBug Java | vs EvoSuite 1.2.0 | Góc A |
| **ID 55** – MutGen (2026, TSE) | primary | HumanEval+LeetCode (synthetic) | vs EvoSuite | Góc B |
| **Experiment (kế hoạch)** | H0/H1 chính thức | CLASSES2TEST | vs EvoSuite 1.2.0 | — |

**Đọc bảng:**
- **ID 9** [p.9, Table IV “AVERAGE OF METRICS”] rất gần nhưng thiếu góc A và C: gpt-4o-mini zero-shot mutation **44.5%** nhưng là metric phụ (không phải RQ chính “muốn biết mutation bao nhiêu?”); không có EvoSuite để so sánh.
- **ID 21** [p.9, Venn diagram] có cả dataset real-world lẫn EvoSuite, nhưng: *“even smaller intersection with both tools in the mutation scores”* — RQ chính là “SBST vs LLM vs Symbolic Execution về tổng thể”, không phải “mutation score là bao nhiêu?”.
- **ID 55** [p.9, Table I] duy nhất đặt mutation là primary: MUTGEN đạt **89.5%** (HumanEval) và **89.1%** (LeetCode) — nhưng trên dataset tổng hợp, không thể so sánh trực tiếp với real-world repos.

### Khoảng trống cụ thể

Chưa có experiment nào đặt câu hỏi **“GPT-4o-mini zero-shot đạt mutation score bao nhiêu trên CLASSES2TEST so với EvoSuite?”** làm H0/H1 chính thức với giao thức đo tường minh (báo cáo rõ tỉ lệ build-fail, phạm vi tính mutation).

## GAP Phụ: GAP-T (Technology)

Không paper nào trong 16 bài được đưa vào thực hiện so sánh chuẩn hóa từ 2 LLM trở lên trên cả Java lẫn Python với cùng bộ đối tượng — **ID 1** (ASTER, 2025, ICSE-SEIP) là gần nhất nhưng chỉ so sánh trong một pipeline và sử dụng bộ đối tượng riêng biệt cho mỗi ngôn ngữ ([p.8, Fig.5] Java SE vs EvoSuite; [p.8, Fig.6] Python vs CodaMosa).

## Chi tiết kiểm tra phản chứng

GAP tuyên bố: Không paper nào đặt mutation score làm primary outcome trên CLASSES2TEST với giao thức đo tường minh (báo cáo rõ tỉ lệ build-fail và phạm vi đo mutation).

| Paper | Mutation là primary? | Giao thức tường minh? | Ghi chú |
|-------|---------------------|----------------------|---------|
| ID 1 – ASTER (2025, ICSE-SEIP) | Không | – | Không báo cáo mutation; chỉ line/branch/method [p.7–8, Fig.5–6] |
| ID 3 – ShreyaBhatia (2024, ICSE Workshop) | Không | – | Chỉ stmt/branch coverage [p.4–5, Table 1–2]; không mutation |
| ID 5 – HITS (2024, ASE) | Không | – | Chỉ line/branch (avg 55.09%/48.12%) [p.7, Table 4–5]; không mutation |
| ID 9 – AgoneTest (2025, ASE) | Không (phụ) | Một phần | Mutation 44.5% (gpt-4o-mini zero-shot) [p.9, Table IV] nhưng secondary. Green-suite-only: Pass **30–38%** [p.9, Sec.VI-B]. Build success: 28.6% zero-shot [p.10, Table V]. Tuyên bố [Sec.VI-B]: *“mutation score chỉ tính trên green suite”* |
| ID 11 – JUnitGenie (2025, ASE) | Không | – | Chỉ branch/line (56.86%/61.45%) [p.8, Table III]; không mutation |
| ID 12 – AgoneTest Workshop (2024, ICSTW) | Không (phụ) | Một phần | Mutation gpt-4 zero-shot **0.546** (54.6%) [p.9, Table V]; human **0.691** (best). Green-suite: pass 36–38% [p.8, Table IV]. Cùng giới hạn green-suite như ID 9. ~66% test classes bị reject [p.10, Sec.C] |
| ID 13 – LinYang (2024, ASE) | Không | – | Chỉ CSR + line/branch; 87.13% defects không detect được do compile fail [p.7]; không mutation |
| ID 15 – RLSQM (2025, DeepTest) | Không | – | Metric: 5 quality metrics, test smells, syntactic correctness; không mutation |
| ID 16 – JsonATG (2025, APSEC) | Không | – | Chỉ coverage + bug count; không mutation |
| ID 21 – Test Wars (2025, ICST) | Không (phụ) | Một phần | Có mutation trong Venn diagram [p.9, Fig.5] nhưng secondary. ChatGPT-4o: compile 57.97% [p.5, Fig.3]. Ghi nhận [p.9]: *“even smaller intersection with both tools in the mutation scores”* |
| ID 24 – CubeTesterAI (2025, ICST) | Không | – | Chỉ code coverage và test pass rate [p.11, Table]; không mutation |
| ID 31 – YutianTang (2023, TSE) | Không | – | Chỉ stmt coverage (50% ChatGPT, 67% EvoSuite) và bug count (44 vs 55/212) [p.7, p.9, Table 6/13]; không mutation |
| ID 32 – DiffPrompting (2023, ASE) | Không | – | Chỉ success rate failure-inducing tests (75.0% vs 28.8% baseline) [p.1]; không mutation |
| ID 41 – CodaMosa (2023, ICSE) | Không | – | Chỉ line/branch (62.6% vs MOSA 43.5%) [p.9]; không mutation |
| ID 55 – MutGen (2026, TSE) | Có | Có | Mutation là primary: MUTGEN **89.5%** (HumanEval) / **89.1%** (LeetCode) [p.9, Table I]. EvoSuite: 69.5%/58.9%. GENvanilla: 77.9%/69.9%. Nhưng trên synthetic datasets, không phải real-world Java repos |
| ID 74 – CLAST (2025, ASE) | Không (phụ) | – | MS 57.21% [p.8] nhưng task tinh chỉnh in-context examples; mutation là metric đánh giá, không phải RQ |

**Kết luận:** GAP-M được xác nhận. **ID 55** duy nhất đặt mutation làm primary (89.5%/89.1% trên synthetic). **ID 9** gần nhất trên CLASSES2TEST nhưng mutation là metric phụ với giao thức green-suite-only (44.5% conditional mutation score). Khoảng trống: chưa có experiment nào đặt mutation làm H0/H1 chính thức trên CLASSES2TEST (Java real-world) với EvoSuite làm baseline.

## Feasibility Check – GAP Chính (GAP-M)

| Tiêu chí | Mức | Ghi chú |
|----------|-----|---------|
| Dataset | An toàn | CLASSES2TEST (ID 9, 12): 10 kho Java, 94 lớp — công khai, tải được ngay từ GitHub; có thể dùng tập con 30–50 lớp để phù hợp thời hạn 2 tuần |
| Công cụ/API | Cần xử lý | GPT-4o-mini: khoảng 0,15 USD/1M token đầu vào; ước tính 50 lớp × 500 token ≈ 0,004 USD. EvoSuite và PIT: mã nguồn mở Java. Rủi ro: cần quản lý giới hạn tốc độ API |
| Tính toán | An toàn | EvoSuite và PIT chạy được trên máy tính xách tay (dựa trên JVM). GPT-4o-mini qua API — không cần GPU |
| Nhãn thực tế | An toàn | Không cần gán nhãn thủ công. Coverage đo bằng JaCoCo; mutation score bằng PIT; executable rate bằng Maven Surefire. Tất cả tự động |
| Kỹ năng | Cần xử lý | Cần biết Java Maven/Gradle, JaCoCo, PIT, OpenAI API. JaCoCo và Maven: có hướng dẫn đầy đủ. PIT: có plugin Maven (pitest-maven); ước tính học thêm dưới 1 tuần |
| Thời gian | An toàn | Pipeline: 3 ngày cài đặt + 3 ngày chạy + 2 ngày phân tích = 8 ngày; xong với bộ đệm từ 6 ngày trở lên trong 2 tuần |
| Đóng góp | An toàn | Kết quả dương: lần đầu đo mutation score trên **toàn bộ** test sinh ra (không loại test lỗi build như ID 9 đã làm) và cung cấp benchmark Java + Python song song với cùng giao thức. Kết quả âm (LLM kém hơn EvoSuite): vẫn có giá trị vì cung cấp điểm dữ liệu tham chiếu cụ thể với giao thức đo chuẩn hóa đầy đủ hơn ID 9 |

**Kết quả:** 0 Chặn / 2 Cần xử lý → An toàn (không quá 2 mục Cần xử lý, không có mục Chặn)
