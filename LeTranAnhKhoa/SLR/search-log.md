                                Search Log - Topic: LLM for Unit Test Case Generation
                                      Thành viên: Lê Trần Anh Khoa - SE192585 
                                            Ngày thực hiện: 2026-06-07

Chuỗi tìm kiếm (Query Strings)
------- String A -------
Query nguyên văn: ("large language model" OR LLM OR GPT-4 OR ChatGPT) AND ("unit test generation" OR "test case generation" OR "automated test generation") AND (Java OR Python)

DATABASE: Google Scholar
Bộ lọc: 
+ Year: 2024 - 2026, English Only, Conference + Journal  
+ Ngày search: 2026-06-07 2:01 PM 
+ Số kết quả: 346 papers
- Sau dedup: 252 papers -> file 01_all_records.csv có 252 dòng
- Screen V1: 0 bị loại -> 252 papers screened -> file 02 có cột v1_decision = 218 EXCLUDE + 32 UNSURE + 2 INCLUDE
- Full-text V2: 0 loại -> 34 final  -> file 03 có cột v2_decision = 34 INCLUDE
(Ghi chú: 32 bài UNSURE + 2 bài INCLUDE từ V1, sau full-text đều đủ tiêu chí)

------- String B -------
Query nguyên văn: ("large language model" OR LLM OR GPT-4) AND ("unit test" OR "test case") AND ("branch coverage" OR "code coverage" OR "mutation score" OR "mutation testing")

DATABASE: Google Scholar
Bộ lọc: 
+ Year: 2024 - 2026, English Only, Conference + Journal  
+ Ngày search: 2026-06-03 02:18 PM 
+ Số kết quả: 161 papers
- Sau dedup: 97 papers -> file 01_all_records.csv có 97 dòng
- Screen V1: 0 bị loại -> 97 papers screened -> file 02 có cột v1_decision = 87 EXCLUDE + 9 UNSURE + 1 INCLUDE
- Full-text V2: 0 loại -> 10 final -> file 03 có cột v2_decision = 10 INCLUDE
(Ghi chú: 9 bài UNSURE + 1 bài INCLUDE từ V1, sau full-text đều đủ tiêu chí)

------- String C -------
Query nguyên văn: ("large language model" OR GPT-4 OR LLM) AND ("unit test generation") AND ("cyclomatic complexity") AND (Java OR Python)

DATABASE: Google Scholar
Bộ lọc: 
+ Year: 2024 - 2026, English Only, Conference + Journal  
+ Ngày search: 2026-06-01 11:46 PM 
+ Số kết quả: 20 papers
- Sau dedup: 3 papers -> file 01_all_records.csv có 3 dòng
- Screen V1: 0 bị loại  -> 3 papers screened -> file 02 có cột v1_decision = 3 EXCLUDE + 0 INCLUDE/Unsure
- Full-text V2: 0 loại -> 0 final -> file 03 có cột v2_decision = 0 INCLUDE

Database                        String                                     Kết quả
Google Scholar                 String A                                     346
Google Scholar                 String B                                     161
Google Scholar                 String C                                     20
Tổng trước dedup                                                            527
Sau dedup nội bộ                                                            352     
Số bị loại (trùng lặp + không phải conference/journal)                      175

## Ghi chú
Thực hiện dedup bằng: [Excel] - Loại trùng dựa trên DOI và tiêu đề bài báo.

Paper trùng nhau nhiều nhất: Các bài báo thuộc hệ thống IEEE Access và ACM Digital Library xuất hiện lặp lại rất nhiều trên kết quả tìm kiếm của Google Scholar

Snowballing tìm được thêm 44 bài báo.
Sau quá trình đánh giá full-text, cả 44 bài đều được INCLUDE.

Tất cả 41 bài UNSURE (32 từ String A + 9 từ String B) sau khi đọc full-text đều đủ tiêu chí, được chuyển thành INCLUDE.

Các điểm bất thường trong quá trình search:

Tỷ lệ bài báo bị loại ở vòng Title/Abstract Screening (V1) rất lớn.Đặc biệt với String A, chỉ 34/252 bài được giữ lại cho bước Full-text Assessment do tiêu đề chứa các keyword "LLM" và "Unit test", nhưng nội dung thực tế lại dùng LLM để giải thích code, sửa bug hoặc sinh System Test/Integration Test chứ không tập trung vào Unit Test Case Generation.

Google Scholar trả về khá nhiều kết quả là các bản pre-print chưa qua bình duyệt trên arXiv (giai đoạn 2025-2026), đòi hỏi phải sàng lọc thủ công rất kỹ để đảm bảo chất lượng bài báo đưa vào danh sách Final.