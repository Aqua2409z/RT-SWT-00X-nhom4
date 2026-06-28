# Search Log – LLMs for Automated Unit Test Generation

**Thành viên:** Trần Minh Quý  
**Ngày thực hiện:** 2026-06-05

---

## Chuỗi tìm kiếm (Query Strings)

### String A

**Query nguyên văn:**

```text
("large language model" OR "LLM" OR "GPT-4" OR "ChatGPT") AND ("unit test generation" OR "test case generation" OR "automated test generation") AND ("Java" OR "Python") site:dl.acm.org
```

**Database:** ACM Digital Library  
**Bộ lọc:** English only, Year 2020-2026  
**Ngày search:** 2026-06-03 20:17  
**Số kết quả:** 441 papers

---

### String B

**Query nguyên văn:**

```text
("large language model" OR "LLM" OR "GPT-4") AND ("unit test" OR "test case") AND ("branch coverage" OR "code coverage" OR "mutation score" OR "mutation testing") site:dl.acm.org
```

**Database:** ACM Digital Library  
**Bộ lọc:** English only, Year 2020-2026  
**Ngày search:** 2026-06-04 04:58  
**Số kết quả:** 309 papers

---

### String C

**Query nguyên văn:**

```text
("LLM" OR "GPT-4" OR "ChatGPT") AND ("unit test generation" OR "test case generation") AND ("Java" OR "Python") AND ("branch coverage" OR "mutation score") site:dl.acm.org
```

**Database:** ACM Digital Library  
**Bộ lọc:** English only, Year 2020-2026  
**Ngày search:** 2026-06-05 05:55  
**Số kết quả:** 92 papers

---

## Tổng hợp trước dedup

| Database | String | Kết quả |
|-----------|---------|---------|
| ACM Digital Library | String A | 441 |
| ACM Digital Library | String B | 309 |
| ACM Digital Library | String C | 92 |
| Snowballing (CrossRef) | Phần S | [Cần điền sau] |
| **Tổng trước dedup** | | **842** |
| **Sau dedup** | | **552** |
| Số bị loại (trùng lặp) | | 290 |

---

## Phần S – Cross-reference Search (Snowballing)

> Snowballing không có query string – không điền vào mục này như các String A/B/C.

**Phương pháp:** Backward snowballing – đọc reference list của các paper đã pass V2 screening

**Thực hiện:** Sau khi có `03_final_included.csv`, đọc reference list của từng paper included → nếu thấy paper mới có vẻ liên quan → lookup trên CrossRef (crossref.org) hoặc Semantic Scholar → thêm vào `01_all_records.csv` với `search_strings = "snowballing"` → đi qua V1 + V2 screening như bình thường

**Công cụ:** CrossRef (crossref.org) để lookup metadata từ DOI; Google Scholar để check forward citations

**Ngày thực hiện:** YYYY-MM-DD

**Paper included đã scan:** [N] paper

**Paper mới phát hiện:** [X] paper pass IC (ghi rõ từ paper nào → tìm được paper nào)

> **Lưu ý:** Snowballing chỉ làm SAU khi hoàn thành tất cả database search. Paper tìm được qua snowball phải đi qua V1+V2 như bình thường – không tự động include vì "được trích dẫn nhiều".

---

## Ghi chú

- Thực hiện dedup bằng: Zotero
- Paper trùng nhau nhiều nhất: [Kiểm tra mục Duplicate Items trong Zotero]
- Snowballing: [số] paper mới tìm được, [số] pass V2
- [Ghi thêm bất kỳ điểm bất thường nào trong quá trình search]

---

## Tiến trình sàng lọc thực tế

### Sau Dedup

- Tổng số record duy nhất: **552**
- Lưu trong: `01_all_records.csv`

### V1 Screening (Title + Abstract)

- Loại: 488 papers
- INCLUDE: 52 papers
- UNSURE: 12 papers
- Giữ lại: **64 papers**

### V2 Screening (Full Text)

- Full-text assessed: 64 papers
- Excluded: 15 papers
- Final included: **48 papers**
