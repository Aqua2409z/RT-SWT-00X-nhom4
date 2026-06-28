##### SLR/gap-analysis.md – Phân tích khoảng trống nghiên cứu (Gap Analysis)
**Thành viên:** Trần Minh Quý
**Chủ đề:** Đánh giá hiệu năng sinh Unit Test tự động của LLM (LLM for Unit Test Case Generation)
**Quy mô minh chứng:** N = 48 bài nghiên cứu (Final Included)

--------------------------------------------------------------------------------

###### 1. Nhận diện 4 Loại Khoảng Trống (GAP Candidates)
Dựa trên phân tích 48 bài báo từ Evidence Table (Bước 2A), nhóm xác định được 4 ứng viên khoảng trống nghiên cứu tiềm năng:

*   **GAP-T (Công nghệ/LLM):** Sự thiếu vắng các đánh giá chuyên sâu và độc lập lấy mô hình **GPT-4** làm trung tâm sinh mã kiểm thử. Đa phần các nghiên cứu quy mô lớn tập trung vào GPT-3.5 hoặc các mô hình mã nguồn mở (như CodeLlama, DeepSeek).
*   **GAP-M (Thước đo/Metric):** Chưa có nghiên cứu nào đo lường và đạt được sự kết hợp đồng thời ngưỡng hiệu năng cao: **Branch Coverage >= 80%** và **Mutation Score >= 60%** khi sử dụng LLM.
*   **GAP-D (Dữ liệu/Dataset):** Các nghiên cứu hiện tại thiếu sự ràng buộc thực nghiệm trực tiếp và đối chứng trên các hàm có **phụ thuộc ngoại vi (Non-standalone / Cần Mocking)**. Hầu hết các bộ dữ liệu hiện hành lảng tránh vấn đề này và chỉ ưu tiên đánh giá trên các hàm thuật toán độc lập (Standalone).
*   **GAP-S (Hạn chế chung/Shared Limitations):** Hơn 40% các bài báo (ví dụ: *Ye Shang 2025* [1], *Chen Yang 2026* [2], *Aminata Diop 2025* [3], *Molinelli 2025* [4]) đều báo cáo hạn chế cốt lõi chung là: **Tỷ lệ LLM sinh mã kiểm thử bị lỗi biên dịch và lỗi cú pháp (ảo tưởng/hallucination) ở mức rất cao**, làm suy giảm nghiêm trọng độ phủ và điểm đột biến thực tế.

--------------------------------------------------------------------------------

###### 2. Kiểm tra phản chứng (Counter-evidence Check)
Bảng dưới đây quét lại Evidence Table để đảm bảo 4 GAP Candidates nêu trên không bị bất kỳ bài báo nào trong số 48 bài minh chứng "phá vỡ" (Bước 2B):

| GAP Candidate | Bằng chứng từ Evidence Table | Có phản chứng không? | Kết luận |
| ------ | ------ | ------ | ------ |
| **GAP-T (Thiếu GPT-4)** | - *Ye Shang (2025)* [1] test 37 LLMs, cao nhất là GPT-3.5.<br>- *Dong Huang (2026)* [5] dùng 12 LLMs, chỉ dùng GPT-4o ngoại tuyến.<br>- *Molinelli (2025)* [4] loại bỏ GPT-4 khỏi thực nghiệm đo Mutation Score do lỗi. | Không. Không có bài báo nào hoàn thiện quy trình sinh test lấy GPT-4 làm cốt lõi. | An toàn (Giữ lại). |
| **GAP-M (Hiệu năng thấp)** | - *Chen Yang (2026)* [2] ghi nhận GPT-4 chỉ đạt 38.43% Branch Coverage.<br>- *Lin Yang (2024)* [6] báo cáo GPT-4 đạt 31.78% Branch Coverage.<br>- *Jiho Shin (2024)* [7] đo GPT-4 đạt 14.98% Mutation Score. | Không. Không có nghiên cứu nào chứng minh GPT-4 có thể chạm tới ngưỡng Branch Cov >= 80% & Mutation Score >= 60%. | An toàn (Giữ lại). |
| **GAP-D (Thiếu hàm Mocking)** | - *Qinghua Xu (2026)* [8] thừa nhận hạn chế khi khái quát hóa sang các bộ dữ liệu phức tạp phụ thuộc vào lớp bên ngoài.<br>- *Sujoy Roy Chowdhury (2024)* [9] nhấn mạnh sự khó khăn khi phân tích các phụ thuộc ngoại vi lúc runtime. | Không. Các bộ dữ liệu lớn hiện nay chủ yếu đánh giá hàm Standalone, lảng tránh hoặc xử lý kém hàm Non-standalone. | An toàn (Giữ lại). |
| **GAP-S (Lỗi biên dịch/cú pháp)** | - *Aminata Diop (2025)* [3] báo cáo tỷ lệ cao test sinh ra thất bại biên dịch.<br>- *Ye Shang (2025)* [1] báo cáo mô hình tốt nhất vẫn bị lỗi cú pháp lên tới 33.62%. | Không. Hầu như bài nào cũng gặp rào cản về khả năng sinh mã thực thi (Runnable/Compilable). | An toàn (Giữ lại). |

--------------------------------------------------------------------------------

###### 3. Đánh giá tính khả thi (Feasibility Check)
Phân tích tính khả thi để giải quyết các GAP trên trong thời gian giới hạn của dự án:

| Tiêu chí | Đánh giá tình trạng | Rủi ro | Giải pháp giảm thiểu (Mitigation) |
| ------ | ------ | ------ | ------ |
| **Dataset (Giải quyết GAP-D)** | Có thể phân loại tập dữ liệu benchmark (Defects4J, Methods2Test) thành 2 tập Standalone và Non-standalone để đối chứng. | An toàn | Khả thi, có thể dùng công cụ phân tích tĩnh hoặc lọc theo thẻ tag sẵn có. |
| **Tool/LLM (Giải quyết GAP-T)** | Gọi API của GPT-4/GPT-4o. | Trung bình | Chuẩn bị kinh phí nhỏ chạy prompt tự động cho tập mẫu các hàm. |
| **Metric (Giải quyết GAP-M)** | Chạy đo lường bằng thư viện chuẩn: JaCoCo (Branch Coverage) và PIT (Mutation Score). | An toàn | Công cụ mã nguồn mở, có sẵn tài liệu tích hợp trên IDE. |
| **Baseline (Cơ sở đối chứng)** | Lấy kết quả của hàm Standalone làm cơ sở đối chứng (Baseline) cho sự suy giảm hiệu năng của hàm Non-standalone. | An toàn | Thiết lập đối chứng nội bộ cùng mô hình, minh bạch và dễ triển khai. |

--------------------------------------------------------------------------------

###### 4. Quyết định GAP cuối cùng (Final GAP Selection)
Theo quy tắc ưu tiên tại Bước 2A và yêu cầu giới hạn của đề tài, nhóm quyết định giữ lại GAP-S như một yếu tố thuộc bối cảnh hạn chế chung, đồng thời tập trung phát triển 3 GAP cốt lõi:

*   **Primary GAP (Khoảng trống cốt lõi): GAP-T và GAP-D.** Sự thiếu vắng các đánh giá chuyên sâu và độc lập về năng lực của GPT-4, đặc biệt là sự thiếu hụt thực nghiệm khi LLM phải xử lý các hàm có phụ thuộc ngoại vi (Non-standalone/Mocking).
*   **Secondary GAP (Khoảng trống phụ): GAP-M.** Các nghiên cứu hiện tại ghi nhận LLM bị mắc kẹt ở ngưỡng hiệu suất rất thấp (chỉ ~31-38% Branch Coverage), chưa có phương pháp nào đột phá chạm được ngưỡng hiệu năng kết hợp Branch Coverage >= 80% và Mutation Score >= 60%.

--------------------------------------------------------------------------------

###### 5. Phát biểu Khoảng trống Nghiên cứu (GAP Statement)
Phân tích từ 48 nghiên cứu hệ thống cho thấy một **khoảng trống công nghệ và dữ liệu (GAP-T, GAP-D)** cốt lõi: Sự thiếu hụt các đánh giá chuyên sâu và độc lập về năng lực của GPT-4 trong việc sinh mã kiểm thử, đặc biệt là khi mô hình phải xử lý các hàm có phụ thuộc ngoại vi phức tạp (Non-standalone) yêu cầu kỹ thuật làm giả dữ liệu (Mocking) thay vì các hàm độc lập đơn giản (Standalone). Bên cạnh đó, tồn tại một **khoảng trống lớn về thước đo hiệu năng (GAP-M)**, khi hầu hết các nghiên cứu hiện tại ghi nhận LLM bị mắc kẹt ở ngưỡng hiệu suất rất thấp (chỉ đạt ~31-38% Branch Coverage và ~14% Mutation Score) cùng tỷ lệ lỗi biên dịch cao (**GAP-S**). Do đó, nghiên cứu này được thiết kế nhằm thiết lập một thực nghiệm đối chứng trực tiếp để đo lường mức độ suy giảm hiệu năng của GPT-4 khi chuyển từ hàm Standalone sang hàm Non-standalone, đồng thời kiểm chứng xem mô hình có thể bứt phá đạt đồng thời ngưỡng Branch Coverage >= 80% và Mutation Score >= 60% hay không.

--------------------------------------------------------------------------------

###### 6. Tài liệu tham khảo (References)
[1] Ye Shang, Quanjun Zhang, Chunrong Fang, Siqi Gu, Jianyi Zhou, Zhenyu Chen. 2025. *A Large-Scale Empirical Study on Fine-Tuning Large Language Models for Unit Testing*. Proc. ACM Softw. Eng. 2, ISSTA. Link: https://doi.org/10.1145/3728951

[2] Chen Yang, Junjie Chen, Bin Lin, Ziqi Wang, Jianyi Zhou. 2026. *Advancing Code Coverage: Incorporating Program Analysis with Large Language Models*. ACM Trans. Softw. Eng. Methodol. Link: https://doi.org/10.1145/3748505

[5] Dong Huang, Jie M. Zhang, Mark Harman, Qianru Zhang, Mingzhe Du, See-Kiong Ng. 2026. *Benchmarking LLMs for Unit Test Generation from Real-World Functions*. ACM Trans. Softw. Eng. Methodol. Link: https://doi.org/10.1145/3805043

[7] Jiho Shin, Sepehr Hashtroudi, Hadi Hemmati, Song Wang. 2024. *Domain Adaptation for Code Model-Based Unit Test Case Generation*. Proc. ISSTA ’24. Link: https://doi.org/10.1145/3650212.3680354

[3] Aminata Diop, Fadel Toure, Mourad Badri. 2025. *From Scenario to Code: Structured Prompting for LLM-Based Unit Test Generation*. WSSE 2025. Link: https://doi.org/10.1145/3779657.3779658

[8] Qinghua Xu, Guancheng Wang, Lionel Briand, Kui Liu. 2026. *Hallucination to Consensus: Multi-Agent LLMs for End-to-End JUnit Test Generation*. ACM Trans. Softw. Eng. Methodol. Link: https://doi.org/10.1145/3803418

[6] Lin Yang, Chen Yang, Shutao Gao, Weijing Wang, Bo Wang, Qihao Zhu, Xiao Chu, Jianyi Zhou, Guangtai Liang, Qianxiang Wang, Junjie Chen. 2024. *On the Evaluation of Large Language Models in Unit Test Generation*. ASE '24. Link: https://doi.org/10.1145/3691620.3695529

[9] Sujoy Roy Chowdhury, Giriprasad Sridhara, A K Raghavan, Joy Bose, Sourav Mazumdar, Hamender Singh, Srinivasan Bajji Sugumaran, Ricardo Britto. 2024. *Static Program Analysis Guided LLM Based Unit Test Generation*. CODS-COMAD Dec ’24. Link: https://doi.org/10.1145/3703323.3703742

[4] Davide Molinelli, Alberto Martin-Lopez, Elliott Zackrone, Beyza Eken, Michael D. Ernst, Mauro Pezzè. 2025. *Tratto: A Neuro-Symbolic Approach to Deriving Axiomatic Test Oracles*. ISSTA 2025. Link: https://doi.org/10.1145/3728960