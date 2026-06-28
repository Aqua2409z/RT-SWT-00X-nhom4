# Evidence Table – LLM for Unit Test Case Generation

| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | Benchmarking LLMs for Unit Test Generation from Real-World Functions, 2026, ACM Trans. Softw. Eng. Methodol. |
| **Link** | [DOI: https://doi.org/10.1145/3805043] |
| **Tool/LLM** | 12 State-of-the-art LLMs |
| **Dataset** | ULT (UnLeakedTestBench) - 3,909 function-level tasks (Python, Cyclomatic Complexity ≥ 10). |
| **Metric** | Accuracy, Statement Coverage, Branch Coverage, Mutation Score. |
| **Kết quả** | Acc: 41.32%, Stmt: 45.10%, Branch: 30.22%, Mutation: 40.21% |
| **Hạn chế** | Data contamination; nhiễu do khác biệt coding style giữa PLT/ULT |
------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | Hallucination to Consensus: Multi-Agent LLMs for End-to-End JUnit Test Generation, 2026, ACM Trans. Softw. Eng. Methodol. |
| **Link** | [DOI: https://doi.org/10.1145/3803418] |
| **Tool/LLM** | CANDOR (Multi-agent framework: Initializer, Planner, Tester, Inspector, etc.) |
| **Dataset** | HumanEvalJava, LeetCodeJava |
| **Metric** | Oracle correctness, Mutation score, Line/Branch coverage |
| **Kết quả** | Vượt trội hơn baseline TOGLL ít nhất 21.1 điểm % về oracle correctness; hiệu suất tương đương EvoSuite về độ bao phủ, tốt hơn về mutation score. |
| **Hạn chế** | N/A |
-------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | Automated Unit Test Generation via Chain-of-Thought Prompt and Reinforcement Learning from Coverage Feedback, 2025, ACM Trans. Softw. Eng. Methodol. |
| **Link** | [DOI: https://doi.org/10.1145/3745765] |
| **Tool/LLM** | TestCTRL (Policy: CodeLlama-7B, Reward: CodeGPT) |
| **Dataset** | Defects4J, CoT Dataset (96K focal methods + prompts) |
| **Metric** | Compilation pass rate, Line coverage, Branch coverage, Bug detection |
| **Kết quả** | Tăng 88.42% line coverage và 88.25% branch coverage so với StarCoder (15.5B); tăng 128.57% khả năng phát hiện lỗi. |
| **Hạn chế** | Giới hạn 2048 tokens; phụ thuộc vào độ chính xác của Reward Model trong việc dự đoán coverage. |
--------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | Reference-Based Retrieval-Augmented Unit Test Generation, 2025, ACM Trans. Softw. Eng. Methodol. |
| **Link** | [DOI:https://doi.org/10.1145/3765758] |
| **Tool/LLM** | RefTest (Sử dụng cơ chế Reference-Based RAG) |
| **Dataset** | 12 open-source Java projects (1,515 methods) |
| **Metric** | Correctness, Completeness, Maintainability (code style, mock usage) |
| **Kết quả** | Vượt trội hơn ChatUniTest, TestPilot, và EvoSuite về độ chính xác và khả năng bao phủ (821 tests được bao phủ hoàn toàn). |
| **Hạn chế** | Phân rã test generation thành các pha Given-When-Then (GWT) để truy xuất ngữ cảnh từ các test case hiện có thay vì chỉ dựa vào code nguồn. |
--------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | Advancing Code Coverage: Incorporating Program Analysis
with Large Language Models, 2025, ACM Trans. Softw. Eng. Methodol. |
| **Link** | [DOI:https://doi.org/10.1145/3748505] |
| **Tool/LLM** | TELPA (TEst generation via LLM and Program Analysis) |
| **Dataset** | 27 open-source Python projects (486 modules, 6,559 branches) |
| **Metric** | CBranch coverage |
| **Kết quả** |Vượt trội hơn Pynguin (SBST), CODAMOSA và CHATTESTER (LLM-based) với mức cải thiện trung bình lần lượt là 34.10%, 25.93% và 21.10%. |
| **Hạn chế** | hạn chế của TELPA trong bài báo: Dừng sớm khi gặp vòng lặp: Ngừng duyệt đồ thị gọi nếu gặp vòng lặp để tránh vô tận, có thể bỏ sót kịch bản đệ quy. Chỉ chọn đường dẫn ngắn nhất: Bỏ qua các đường dẫn dài hơn có thể chứa thông tin cần thiết để giải quyết ràng buộc nhánh. Chi phí vận hành: Đắt đỏ do dùng LLM, nên chỉ kích hoạt khi công cụ truyền thống đã thất bại. Phụ thuộc vào phân tích tĩnh: Nếu phân tích chương trình sai, LLM sẽ nhận thông tin không chính xác. Lựa chọn ngẫu nhiên: Cơ chế thay thế chuỗi gọi (sequence) khi thất bại còn mang tính ngẫu nhiên, chưa tối ưu. |
--------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | REACCEPT: Automated Co-evolution of Production and Test Code Based on Dynamic Validation and Large Language Models, 2025, Proc. ACM Softw. Eng. 2, ISSTA |
| **Link** | [DOI:https://doi.org/10.1145/3728930] |
| **Tool/LLM** | ReAccept |
| **Dataset** | 537 Java projects (23,403 mẫu) |
| **Metric** | Accuracy (độ chính xác của việc xác định và cập nhật test code) |
| **Kết quả** |Đạt 60.16% độ chính xác trong việc xác định và cập nhật test code (cao hơn 90% so với kỹ thuật CEPROT). |
| **Hạn chế** | Thiên kiến nhận thức: LLM mặc định mọi thay đổi mã nguồn đều cần cập nhật test, gây ra các đề xuất không cần thiết. Độ chính xác: Tỷ lệ thành công khi vừa xác định vừa cập nhật là 60.16%, vẫn tồn tại gần 40% thất bại. Sự phụ thuộc: Cần môi trường thực thi (JUnit, JaCoCo) để xác thực; nếu dự án thiếu cấu hình này, phương pháp không thể hoạt động. Rủi ro tạo code sai: LLM có thể xuất ra mã lỗi, cần cơ chế lặp lại để sửa chữa thay vì đảm bảo đúng ngay từ đầu.|
-------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | An Empirical Evaluation of Using Large Language Models for Automated Unit Test Generation, 2023, IEEE Transactions on Software Engineering (TSE)|
| **Link** | [DOI:https://doi.org/10.1109/tse.2023.3334955] |
| **Tool/LLM** |TestPilot |
| **Dataset** | 25 npm packages (JavaScript), 1,684 API functions|
| **Metric** | Statement coverage, Branch coverage |
| **Kết quả** |Median statement coverage 70.2%, branch coverage 52.8%. Vượt trội so với Nessie (51.3% và 25.6%). |
| **Hạn chế** | Dữ liệu phức tạp: Khó tạo test cho các hàm có cấu trúc input phức tạp hoặc khó mock phụ thuộc. Điểm yếu ngôn ngữ: Code JavaScript động khiến LLM khó đoán chính xác kiểu dữ liệu (type), dễ sinh ra test không hợp lệ. Thiếu tư duy kiểm thử: Test tạo ra thường chỉ chạy được (pass) chứ không kiểm tra đúng logic nghiệp vụ hoặc các case biên. Chi phí vận hành: Việc lặp lại quá trình sửa lỗi (repair) dựa trên feedback tốn nhiều thời gian và token.|
-------


| Thuộc tính | Nội dung |
| :--- | :--- |
| **Paper** | Prompt engineering in LLMs for automated unit test generation: A large-scale study, 2026, Empirical Software Engineering |
| **Link** | [DOI:https://doi.org/10.1007/s10664-026-10840-4] |
| **Tool/LLM** |GPT-3.5, GPT-4, Mistral 7B, Mixtral 8x7B (đối chứng với EvoSuite)|
| **Dataset** | Defects4J, SF110, và CMD (216,300 test cases)|
| **Metric** | Tính đúng đắn cú pháp, khả năng biên dịch, lỗi ảo giác, độ đọc hiểu, độ bao phủ code, và test smells |
| **Kết quả** |GToT (Guided Tree-of-Thought) cải thiện đáng kể độ tin cậy và khả năng biên dịch; test do LLM tạo ra dễ đọc hơn công cụ truyền thống nhưng vẫn tồn tại lỗi thiết kế. |
| **Hạn chế** | Tỷ lệ lỗi biên dịch cao: Lỗi ảo giác (gọi sai API, thiếu tham chiếu) lên tới 86%. Thiếu khả năng bảo trì: Vẫn tồn tại các "Test Smells" như Magic Numbers và Assertion Roulette. Cần hỗ trợ ngoại vi: Cần kết hợp kiểm chứng tự động và tinh chỉnh tìm kiếm để đảm bảo độ tin cậy.|



