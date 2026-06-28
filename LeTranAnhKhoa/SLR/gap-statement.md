# Gap Statement – LLM for Unit Test Generation

**Evidence table:** N = 44 papers

## Các khoảng trống phát hiện

### GAP‑T (Technology): Thiếu benchmark thống nhất và thiếu đa dạng ngôn ngữ
- **Mô tả:** 
Các nghiên cứu hiện nay sử dụng nhiều LLM khác nhau (GPT‑4, GPT‑4o, LLaMA, DeepSeek, Claude…). Tuy nhiên, chúng thiếu một benchmark chuẩn (cùng dataset, cùng metric) để có thể so sánh trực tiếp các mô hình với nhau. Bên cạnh đó, vấn đề đa dạng ngôn ngữ lập trình chưa được quan tâm đúng mức. Phần lớn dataset hiện có tập trung vào Java (Defects4J 5→15, Bears, GitBug‑Java, Apache Commons) và Python (LeetCode, Codeforces, các benchmark Python). Rất ít bài sử dụng C++, C#, JavaScript, Go…

- **Bằng chứng (cột Tool/LLM, Metric, Kết quả – kèm link DOI/URL):**
Các bài có chỉ rõ GPT‑4/GPT‑4o: TestEval (GPT‑4, GPT‑4o), Test Wars (ChatGPT‑based), Framework (GPT‑4o), TestGenEval (GPT‑4o), On the Evaluation (ChatGPT‑4o), Comparative Study (GPT‑4o, Llama 3.1, DeepSeekCoder), Disrupting Test Development (GPT‑4, ChatGPT, Copilot, Tabnine), TestCase‑Eval (GPT‑4, GPT‑4o), Scenario‑Driven (GPT‑4).

Điểm yếu chung: Các bài này sử dụng dataset và metric khác nhau, không thể so sánh ngang giữa các LLM hoặc giữa LLM với các phương pháp khác. Ngoài ra, do thiếu tập dữ liệu đa ngôn ngữ, tính tổng quát của các kết quả đánh giá còn hạn chế.



### GAP‑A (Prompt Engineering & Assessment): Thiếu đánh giá có hệ thống về chiến lược prompting và thiếu tiêu chuẩn thống nhất để đo lường hiệu quả sinh unit test của GPT‑4

- **Mô tả:** Trong 44 bài báo, mặc dù có một số nghiên cứu bước đầu thử nghiệm các kỹ thuật prompting khác nhau (zero‑shot, few‑shot, chain‑of‑thought, v.v.), nhưng chưa có nghiên cứu nào so sánh một cách có hệ thống các chiến lược prompting trên GPT‑4 để tối ưu đồng thời Branch Coverage và Mutation Score. Hơn nữa, các metric đánh giá (coverage, mutation score, correctness rate, F1, …) được sử dụng không đồng nhất giữa các bài, dẫn đến khó khăn trong việc so sánh và tổng hợp kết quả. Đặc biệt, chưa có một **khung đánh giá chuẩn** nào được đề xuất hoặc áp dụng rộng rãi cho việc sinh unit test bằng LLM trong bối cảnh giáo dục.

- **Bằng chứng (cột Tool/LLM, Metric, Kết quả – kèm link DOI/URL):**
  **1. Các bài có thử nghiệm prompting nhưng chưa có so sánh có hệ thống:**
  - *Aster* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11121720/): Sử dụng LLM với hướng dẫn từ phân tích tĩnh, không so sánh các chiến lược prompting.
  - *TestEval* (2025) – [DOI](https://aclanthology.org/2025.findings-naacl.197/): Đánh giá 17 LLM trên ba mức coverage (line/branch/path), nhưng không thay đổi prompt.
  - *TestGenEval* (2025) – [DOI](https://proceedings.iclr.cc/paper_files/paper/2025/hash/26ded5c8ee8ec1bc4caced4e1c9b1584-Abstract-Conference.html): Đánh giá GPT‑4o và các LLM khác, dùng prompt đơn giản, không so sánh chiến lược.
  - *Test Wars* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/10989033/): So sánh LLM‑based với SBST, không tập trung vào prompting.
  - *CasModaTest* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11229491/): Model‑agnostic, không tối ưu prompt cho GPT‑4.
  - *Framework and Performance Evaluation* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11232178/): Đánh giá GPT‑4o trên TestEval, dùng prompt mặc định.
  - *Comparative Study* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11166964/): So sánh GPT‑4o với Llama, DeepSeek, không thay đổi prompt.
  - *Disrupting test development* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11101520/): So sánh GPT‑4, Copilot, ChatGPT, Tabnine, prompt không được mô tả chi tiết.
  - *TestCase‑Eval* (2025) – [DOI](https://aclanthology.org/2025.acl-short.82/): Đánh giá 19 LLMs trên fault coverage, không thay đổi prompt.
  - *On the Evaluation of Test Suites* (2025) – [DOI](https://link.springer.com/chapter/10.1007/978-3-032-05188-2_16): So sánh 5 LLMs (ChatGPT‑4o, …) trên 12 Python programs, có thử nghiệm hai cách prompting nhưng chưa phải so sánh có hệ thống.
  - *What Inputs Drive Effective LLM‑based Unit Test Generation?* (2026) – [DOI](https://ieeexplore.ieee.org/abstract/document/11205490/): Nghiên cứu ảnh hưởng của đầu vào (code, documentation, test context) nhưng không so sánh các chiến lược prompting.
  - *Balancing Cost and Quality in LLM‑based Test Case Generation by Prompt Engineering* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11391174/): Đề cập đến prompt engineering để cân bằng chi phí và chất lượng, nhưng không có so sánh định lượng giữa các chiến lược.

  **2. Các bài tập trung vào metric đánh giá nhưng thiếu thống nhất:**
  - *MutGen* (2026) – [DOI](https://ieeexplore.ieee.org/abstract/document/11478734/): Dùng mutation score, không dùng coverage.
  - *SAGEN* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11042526/): Dùng code coverage + test case quality, không định nghĩa rõ quality.
  - *LLMTestCraft* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11351817/): Dùng correctness rate (92.6%), không dùng coverage hay mutation score.
  - *Three‑Stage Pipeline* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11351825/): Dùng code coverage, branch coverage, execution success rate.
  - *MUATC* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11396580/): Dùng coverage, so sánh với Coverup.
  - *NexuSym* (2025) – [DOI](https://link.springer.com/article/10.1007/s10515-025-00529-1): Dùng test coverage (cải thiện đến 20%).
  - *Deep Path Coverage* (2026) – [DOI](https://www.mdpi.com/2073-8994/18/3/455): Dùng code coverage, branch coverage, mutation score nhưng không tách biệt cho GPT‑4.

### GAP‑E (Executability & Cost): Thiếu báo cáo về tỷ lệ test chạy được (executability rate) và chi phí sử dụng API

- **Mô tả:** Trong 44 bài báo, hầu hết tập trung vào các chỉ số coverage (line, branch, method) và mutation score. Tuy nhiên, **rất ít bài báo cáo tỷ lệ test case sinh ra có thể biên dịch và chạy thành công** (executability rate) – một chỉ số quan trọng để đánh giá tính khả thi của test case trong thực tế. Hơn nữa, **không có bài nào báo cáo chi phí sử dụng API** (số token đầu vào/đầu ra, thời gian sinh, chi phí USD) cho GPT‑4/GPT‑4o – một yếu tố then chốt nếu muốn áp dụng trong giáo dục với quy mô lớn.

- **Bằng chứng (cột Metric, Kết quả – kèm link DOI/URL):**

  **1. Các bài có báo cáo executability rate (chỉ 2 bài):**
  - *Three‑Stage Pipeline* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11351825/): Báo cáo execution success rate (78%–83%).
  - *Framework and Performance Evaluation* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11232178/): Có chỉ số test execution ratio (TE), nhưng không phải là tỷ lệ chạy được trên tổng số test sinh ra.
  - *Các bài còn lại (42 bài)*: **Không báo cáo** bất kỳ chỉ số nào về tỷ lệ test chạy được.

  **2. Các bài có thể có dữ liệu về executability (nhưng không báo cáo):**
  - *TestGenEval* (2025) – [DOI](https://proceedings.iclr.cc/paper_files/paper/2025/hash/26ded5c8ee8ec1bc4caced4e1c9b1584-Abstract-Conference.html): Có thực thi test, nhưng chỉ báo cáo coverage, không báo cáo tỷ lệ chạy được.
  - *TestEval* (2025) – [DOI](https://aclanthology.org/2025.findings-naacl.197/): Có thực thi test trên 210 chương trình, nhưng chỉ báo cáo coverage.
  - *Test Wars* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/10989033/): So sánh SBST và LLM, có thực thi test nhưng không báo cáo executability rate.
  - *MutGen* (2026) – [DOI](https://ieeexplore.ieee.org/abstract/document/11478734/): Tập trung vào mutation score, không đề cập đến tính khả thi của test.
  - *AUGER* (2025) – [DOI](https://ieeexplore.ieee.org/abstract/document/11029864/): Chỉ báo cáo F1 và precision, không có executability.

### Phát biểu GAP tổng hợp 

> *Although recent studies have explored LLM‑based unit test generation, no study has systematically evaluated GPT‑4/GPT‑4o against manually written student test cases on Java/Python functions with medium cyclomatic complexity, using both Branch Coverage and Mutation Score as primary evaluation metrics under a unified experimental setting.*