# Search Log – LLM for Unit Test Case Generation
**Thành viên:** Trần Bích Trâm
**Ngày thực hiện:** 2026-06-04

---

## Chuỗi tìm kiếm (Query Strings)

### String A
**Query nguyên văn:**

("large language model" OR LLM OR GPT-4 OR ChatGPT)
AND
("unit test generation" OR "test case generation" OR "automated test generation")
AND
(Java OR Python)


**Database:** OpenAlex
**Bộ lọc:** Year 2020-2026, English only, Conference + Journal 
**Ngày search:** 2026-06-04 18:42
**Số kết quả:** 13 papers

---

### String B
**Query nguyên văn:**

("large language model" OR LLM OR GPT-4)
AND
("unit test" OR "test case")
AND
("branch coverage" OR "code coverage" OR "mutation score" OR "mutation testing")

**Database:** OpenAlex
**Bộ lọc:** Year 2020-2026, English only, Conference + Journal 
**Ngày search:** 2026-06-04 18:49
**Số kết quả:** 30 papers

---

### String C
**Query nguyên văn:**

("LLM" OR "Large Language Model" OR "Generative AI" OR "GPT")
AND
("unit test" OR "test case" OR "automated test" OR "test generation")
AND
(Java OR Python OR "source code")
AND
("coverage" OR "mutation")


**Database:** OpenAlex
**Bộ lọc:** Year 2020-2026, English only, Conference + Journal 
**Ngày search:** 2026-06-04 19:05
**Số kết quả:** 24 papers

---

## Tổng hợp trước dedup

| Database | String | Kết quả |
|----------|--------|---------|
| OpenAlex | String A | 13 |
| OpenAlex | String B | 30 |
| OpenAlex | String C | 24 |
| **Tổng trước dedup** | | **67** |
| **Sau dedup** | | **51** |
| Số bị loại (trùng lặp) | | 16 |

---

## Ghi chú

- Thực hiện dedup bằng:  Excel
- Paper trùng nhau nhiều nhất: các paper trên ACM Transa và Proceedings.
- Logic check paper trùng: Title+ Author + Year/DOI
- Xử lý ngoại lệ: Các bài báo bị trùng lặp tiêu đề nhưng có DOI khác nhau đã được kiểm tra thủ công để xác định xem đó là bài báo mở rộng hay trùng lặp thực sự.
- Công cụ hỗ trợ: Ngoài Excel, các hàm lọc (filter) và chức năng kiểm tra định dạng được sử dụng để loại bỏ các khoảng trắng thừa, giúp tăng hiệu quả so khớp tiêu đề.
- Tính nhất quán: Sau khi thực hiện dedup, tổng số bài báo đã giảm từ 67 xuống còn 51, đảm bảo tính chính xác cho các bước đánh giá tiếp theo.

