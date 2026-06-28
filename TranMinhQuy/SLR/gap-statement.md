--------------------------------------------------------------------------------
### SLR/gap-analysis.md – Phân tích khoảng trống nghiên cứu (Gap Analysis)
**Thành viên:** Trần Minh Quý
**Chủ đề:** Đánh giá hiệu năng sinh Unit Test tự động của LLM (LLM for Unit Test Case Generation)
**Quy mô minh chứng:** N = 48 bài nghiên cứu (Final Included)

---

#### 1. Nhận diện 4 Loại Khoảng Trống (GAP Candidates)
Dựa trên phân tích 48 bài báo từ Evidence Table (Bước 2A), nhóm xác định được 4 ứng viên khoảng trống nghiên cứu tiềm năng:

*   **GAP-T (Công nghệ/LLM):** Sự thiếu vắng các đánh giá chuyên sâu và độc lập lấy mô hình **GPT-4** làm trung tâm sinh mã kiểm thử. Đa phần các nghiên cứu quy mô lớn tập trung vào GPT-3.5 hoặc các mô hình mã nguồn mở (như CodeLlama, DeepSeek).
*   **GAP-M (Thước đo/Metric):** Chưa có nghiên cứu nào đo lường và đạt được sự kết hợp đồng thời ngưỡng hiệu năng cao: **Branch Coverage ≥ 80%** và **Mutation Score ≥ 60%** khi sử dụng LLM.
*   **GAP-D (Dữ liệu/Dataset):** Các nghiên cứu hiện tại thiếu sự ràng buộc thực nghiệm trực tiếp trên các hàm Java/Python có dải **Độ phức tạp vòng trung bình (Cyclomatic Complexity - CC từ 5-15)**.
*   **GAP-S (Hạn chế chung/Shared Limitations):** Hơn 40% các bài báo (ví dụ: *Ye Shang (2025)* [5], *Chen Yang (2026)*, *Aminata Diop (2025)*, *Molinelli (2025)*) đều báo cáo hạn chế cốt lõi chung là: **Tỷ lệ LLM sinh mã kiểm thử bị lỗi biên dịch và lỗi cú pháp (ảo tưởng/hallucination) ở mức rất cao**, làm suy giảm nghiêm trọng độ phủ và điểm đột biến thực tế.

---

#### 2. Kiểm tra phản chứng (Counter-evidence Check)
Bảng dưới đây quét lại Evidence Table để đảm bảo 4 GAP Candidates nêu trên không bị bất kỳ bài báo nào trong số 48 bài minh chứng "phá vỡ" (Bước 2B):

| GAP Candidate | Bằng chứng từ Evidence Table | Có phản chứng không? | Kết luận |
| :--- | :--- | :--- | :--- |
| **GAP-T (Thiếu GPT-4)** | - *Ye Shang (2025)* [5] test 37 LLMs, cao nhất là GPT-3.5.<br>- *Dong Huang (2026)* dùng 12 LLMs, chỉ dùng GPT-4o ngoại tuyến để sửa lỗi.<br>- *Molinelli (2025)* dùng GPT-4 nhưng phải **loại bỏ** khỏi thực nghiệm vì sinh ra quá nhiều test lỗi biên dịch. | **Không.** Không có bài báo nào hoàn thiện quy trình sinh test lấy GPT-4 làm cốt lõi. | ✅ **An toàn** (Giữ lại). |
| **GAP-M (Hiệu năng thấp)** | - *Chen Yang (2026)* đo GPT-4 chỉ đạt **38.43% Branch Coverage**.<br>- *Lin Yang (2024)* ghi nhận GPT-4 chỉ đạt **31.78% Branch Coverage**.<br>- *Jiho Shin (2024)* báo cáo GPT-4 chỉ đạt **14.98% Mutation Score**. | **Không.** Không có nghiên cứu nào chứng minh GPT-4 có thể chạm tới ngưỡng Branch Cov ≥ 80% & Mutation Score ≥ 60%. | ✅ **An toàn** (Giữ lại). |
| **GAP-D (Thiếu dải CC 5-15)** | - *Dong Huang (2026)* báo cáo CC trung bình = 14.87, nhưng họ lại không dùng GPT-4 để sinh test.<br>- Các bài của Meta (*Nadia Alshahwan 2024*) và *Ye Shang (2025)* [5] hoàn toàn không báo cáo (Not Reported) độ phức tạp CC. | **Không.** Không có bài nào ghép nối trực tiếp GPT-4 với dải CC 5-15. | ✅ **An toàn** (Giữ lại). |
| **GAP-S (Lỗi biên dịch/cú pháp)** | - *Aminata Diop (2025)* báo cáo 64% test sinh ra thất bại biên dịch.<br>- *Ye Shang (2025)* [5] báo cáo mô hình tốt nhất vẫn bị lỗi cú pháp lên tới 33.62%. | **Không.** Hầu như bài nào cũng gặp rào cản về khả năng sinh mã thực thi (Runnable/Compilable). | ✅ **An toàn** (Giữ lại). |

---

#### 3. Đánh giá tính khả thi (Feasibility Check)
Phân tích tính khả thi để giải quyết các GAP trên trong thời gian giới hạn của dự án:

| Tiêu chí | Đánh giá tình trạng | Rủi ro | Giải pháp giảm thiểu (Mitigation) |
| :--- | :--- | :--- | :--- |
| **Dataset (Giải quyết GAP-D)** | Hoàn toàn có thể dùng công cụ Lizard quét các benchmark (Defects4J, Methods2Test) để lọc tự động tập hàm có CC 5-15. | ✅ An toàn | Khả thi, không cần gán nhãn thủ công. |
| **Tool/LLM (Giải quyết GAP-T)** | Gọi API của GPT-4/GPT-4o. | ⚠️ Trung bình | Chuẩn bị kinh phí nhỏ (< $10) chạy prompt tự động cho tập mẫu ≥50 hàm. |
| **Metric (Giải quyết GAP-M)** | Chạy đo lường bằng thư viện chuẩn: JaCoCo (Branch Coverage) và PIT/pytest-mutagen (Mutation Score). | ✅ An toàn | Công cụ mã nguồn mở, có sẵn tài liệu tích hợp trên IDE. |
| **Baseline (Cơ sở đối chứng)** | Lấy dữ liệu test thủ công của sinh viên và sinh tự động từ Randoop. | ⚠️ Trung bình | Thu thập assignment sinh viên; Randoop cài cắm và sinh tự động nhanh chóng. |

---

#### 4. Quyết định GAP cuối cùng (Final GAP Selection)
Theo quy tắc ưu tiên tại Bước 2A (GAP-T > GAP-M > GAP-D > GAP-S) và yêu cầu giới hạn tối đa 2 GAP, nhóm quyết định **loại bỏ GAP-S và gộp GAP-D vào điều kiện môi trường**. Kết quả chốt hạ 2 GAP cuối cùng:

*   **Primary GAP (Khoảng trống cốt lõi): GAP-T.** Khoảng trống công nghệ về việc thiếu vắng các đánh giá độc lập, chuyên sâu đo lường năng lực của GPT-4 trong việc sinh mã kiểm thử đơn vị.
*   **Secondary GAP (Khoảng trống phụ): GAP-M.** Khoảng trống về thước đo khi các nghiên cứu có sử dụng LLM hiện tại (bao gồm cả GPT-4) đều đang ghi nhận kết quả rất thấp (Branch Coverage ~38%, Mutation Score ~14%), chưa có phương pháp nào đột phá chạm được ngưỡng hiệu năng kết hợp Branch Coverage ≥ 80% và Mutation Score ≥ 60%.

---

#### 5. Phát biểu Khoảng trống Nghiên cứu (GAP Statement)
Phân tích từ 48 nghiên cứu hệ thống cho thấy một **khoảng trống công nghệ (GAP-T)** cốt lõi: Chưa có một thực nghiệm chuyên sâu nào lấy mô hình GPT-4 làm trung tâm để đánh giá độc lập năng lực sinh unit test trên các hàm Java/Python. Bên cạnh đó, tồn tại một **khoảng trống lớn về thước đo hiệu năng (GAP-M)**, khi hầu hết các nghiên cứu hiện tại ghi nhận LLM bị mắc kẹt ở ngưỡng hiệu suất rất thấp (chỉ đạt ~31-38% Branch Coverage và ~14% Mutation Score) và gặp tỷ lệ lỗi biên dịch cao. Do đó, nghiên cứu này được thiết kế để thiết lập một thực nghiệm đối chứng trực tiếp (GPT-4 vs. Randoop vs. Lập trình viên là sinh viên) trên các hàm có dải độ phức tạp trung bình (Cyclomatic Complexity 5-15), nhằm kiểm chứng khả năng phá vỡ giới hạn hiện tại, đạt đồng thời hai ngưỡng hiệu năng cao: **Branch Coverage ≥ 80%** và **Mutation Score ≥ 60%**.

---

#### 6. Tài liệu tham khảo (References)
[5] Ye Shang, Quanjun Zhang, Chunrong Fang, Siqi Gu, Jianyi Zhou, Zhenyu Chen. 2025. A Large-Scale Empirical Study on Fine-Tuning Large Language Models for Unit Testing. *Proc. ACM Softw. Eng. 2*, ISSTA, Article ISSTA074 (July 2025). Link: https://doi.org/10.1145/3728951