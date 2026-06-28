# IE Criteria - LLM for Unit Test Generation
**Thành viên:** Trương Dan Huy
**RQ:** "Do LLM-based techniques generate unit test cases for Java/Python with measurable effectiveness in coverage or mutation score?"
**PICO:** P=software unit testing; unit test generation; test case generation; automated test generation | I=LLM; Large Language Model; GPT-4; ChatGPT; AI/NLP-based test generation techniques | C=traditional automated test generation tools; baseline methods; human-written tests; or no comparison if measurable results are reported | O=branch coverage; code coverage; mutation score; mutation testing result; generated test quality; effectiveness of generated unit tests

---

## Inclusion Criteria (IC) - paper PHẢI có đủ tất cả

| Mã       | Tiêu chí                                                                                                                                                                            |
| -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **IC-L** | Viết bằng tiếng Anh                                                                                                                                                                 |
| **IC-Y** | Xuất bản từ 2020 đến nay - Lý do: LLM/GPT-based software engineering phát triển mạnh từ sau 2020                                                                                    |
| **IC-T** | Đăng trên conference, journal, workshop, symposium, hoặc preprint học thuật - không phải blog, slide, thesis, báo cáo môn học, hoặc tài liệu quảng cáo                              |
| **IC-P** | Về task: unit test generation, test case generation, hoặc automated test generation trong software testing, có bối cảnh lập trình hoặc đánh giá liên quan đến Java/Python           |
| **IC-I** | Dùng kỹ thuật: LLM, Large Language Model, GPT-4, ChatGPT, hoặc AI/NLP-based technique liên quan trực tiếp đến sinh test case                                                        |
| **IC-E** | Có ít nhất 1 kết quả thực nghiệm trong table, figure, hoặc phần kết quả, như coverage, mutation score, pass rate, assertion quality, correctness, hoặc số lượng test case sinh được |

## Exclusion Criteria (EC) - loại nếu BẤT KỲ điều kiện nào đúng

| Mã       | Tiêu chí                                                                                                                                                                                                                       |
| -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **EC-D** | Trùng lặp với paper đã có trong danh sách; ưu tiên giữ bản đầy đủ hơn, mới hơn, hoặc có DOI rõ ràng hơn                                                                                                                        |
| **EC-A** | Không truy cập được full-text hoặc không đủ thông tin để đánh giá IC/EC                                                                                                                                                        |
| **EC-S** | Dưới 4 trang, extended abstract, poster, short paper, hoặc nghiên cứu quá ngắn không có thực nghiệm rõ ràng                                                                                                                    |
| **EC-N** | Không có thực nghiệm, ví dụ position paper, vision paper, tutorial, editorial, opinion paper, hoặc survey thuần túy                                                                                                            |
| **EC-O** | Không về topic: nói về test execution, debugging, maintenance, code repair, code summarization, requirement analysis, hoặc code generation chung; hoặc có dùng LLM/AI nhưng không tập trung vào unit test/test case generation |

Checklist tự kiểm (trước khi bắt đầu screening):

- [ ] Đủ 6 IC và 5 EC?
- [ ] IC-P là task cụ thể: unit test generation/test case generation/automated test generation?
- [ ] IC-I là kỹ thuật cụ thể: LLM, GPT-4, ChatGPT, hoặc AI/NLP-based liên quan trực tiếp đến sinh test?
- [ ] EC-O mô tả ít nhất 2 hướng lạc để có thể nhầm với topic?
- [ ] IC-Y có lý do chọn năm 2020 trở đi?

