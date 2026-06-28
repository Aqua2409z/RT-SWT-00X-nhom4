# FILE 4: prisma-flow.md — Số phải khớp CSV

[Records từ database searching (N = 527)]  ← Tổng từ search-log.md (String A: 346, String B: 161, String C: 20)
      ↓
[Sau khi xóa duplicate (N = 352)]          ← = dòng trong 01_all_records.csv
      ↓
┌────────────────────────────────────────────────────────────────────────┐
│ Screened title + abstract (N = 352)                                    │
│  └── Excluded (N = 308): EC-A=97, EC-S=15, EC-N=10, EC-O=74,           │
│      IC-E=97, IC-T=15                                                  │
└────────────────────────────────────────────────────────────────────────┘
      ↓ 44 papers pass  ← = INCLUDE + Unsure trong 02
┌────────────────────────────────────────────────────────────────────────┐
│ Full-text assessed (N = 88)                                            │
│  ├── Từ Database tìm kiếm: N = 44 (34 String A + 10 String B + 0 C)    │
│  ├── Từ Snowballing tìm thêm: N = 44                                   │
│  └── Excluded (N = 0)                                                  │
└────────────────────────────────────────────────────────────────────────┘
      ↓
[Final included (N = 88)]                 ← = Include trong 03_final_included.csv

## Kiểm tra nhất quán (tự check trước khi nộp):

- Rows trong 01 CSV = N sau dedup (352 dòng) ✓
- Count(v1_decision = EXCLUDE) trong 02 = Excluded vòng 1 (308 dòng) ✓
- Count(v1 = INCLUDE + Unsure) = Full-text assessed từ database (44 dòng) ✓
- Count(v2_decision = Include) trong 03 = Final included (88 dòng bao gồm cả bài từ database và snowballing) ✓

## Thống kê chi tiết theo chuỗi tìm kiếm (Query Strings)

### Chuỗi tìm kiếm A (String A)
- **Số kết quả ban đầu:** 346 papers
- **Sau khi xóa trùng (Dedup):** 252 papers (dòng trong 01_all_records.csv)
- **Sàng lọc vòng 1 (Screen V1):** 252 papers screened  
  └── `v1_decision` = 218 EXCLUDE + 32 UNSURE + 2 INCLUDE
- **Đánh giá toàn văn vòng 2 (Full-text V2):** 34 bài đưa vào đánh giá (32 UNSURE + 2 INCLUDE từ V1)  
  └── 0 bài bị loại -> `v2_decision` = 34 INCLUDE

### Chuỗi tìm kiếm B (String B)
- **Số kết quả ban đầu:** 161 papers
- **Sau khi xóa trùng (Dedup):** 97 papers (dòng trong 01_all_records.csv)
- **Sàng lọc vòng 1 (Screen V1):** 97 papers screened  
  └── `v1_decision` = 87 EXCLUDE + 9 UNSURE + 1 INCLUDE
- **Đánh giá toàn văn vòng 2 (Full-text V2):** 10 bài đưa vào đánh giá (9 UNSURE + 1 INCLUDE từ V1)  
  └── 0 bài bị loại -> `v2_decision` = 10 INCLUDE

### Chuỗi tìm kiếm C (String C)
- **Số kết quả ban đầu:** 20 papers
- **Sau khi xóa trùng (Dedup):** 3 papers (dòng trong 01_all_records.csv)
- **Sàng lọc vòng 1 (Screen V1):** 3 papers screened  
  └── `v1_decision` = 3 EXCLUDE + 0 INCLUDE/Unsure
- **Đánh giá toàn văn vòng 2 (Full-text V2):** 0 bài đưa vào đánh giá  
  └── 0 bài final -> `v2_decision` = 0 INCLUDE

---

## Ghi chú bổ sung & Các điểm bất thường

- **Phương pháp xóa trùng (Dedup):** Thực hiện bằng [Excel] - Loại trùng lặp dựa trên DOI và tiêu đề bài báo. Các bài báo thuộc hệ thống IEEE Access và ACM Digital Library xuất hiện lặp lại rất nhiều trên kết quả tìm kiếm của Google Scholar.
- **Snowballing:** Tìm kiếm mở rộng (Snowballing) phát hiện thêm **44 bài báo**. Sau quá trình đánh giá toàn văn (Full-text Assessment), cả 44 bài đều đạt tiêu chuẩn và được **INCLUDE**.
- **Tỷ lệ loại ở vòng Title/Abstract (V1) rất lớn:** Đặc biệt với String A, chỉ có 34/252 bài được giữ lại cho bước Full-text do tiêu đề chứa các keyword "LLM" và "Unit test", nhưng nội dung thực tế lại sử dụng LLM để giải thích mã nguồn, sửa lỗi (bug fixing) hoặc sinh Test hệ thống/Tích hợp (System/Integration Test) chứ không tập trung vào sinh kiểm thử đơn vị (Unit Test Case Generation).
- **Yêu cầu sàng lọc thủ công kỹ lưỡng:** Google Scholar trả về khá nhiều kết quả là các bản pre-print chưa qua bình duyệt trên arXiv (giai đoạn 2025-2026), đòi hỏi phải sàng lọc thủ công rất kỹ để đảm bảo chất lượng học thuật của danh sách bài báo cuối cùng (Final list).

## Nhật ký mã hóa tiêu chí (Criteria Log Codes)
- 97 EC-A
- 15 EC-S
- 10 EC-N
- 74 EC-O
- 3 IC-L
- 5 IC-Y
- 15 IC-T
- 106 IC-I
- 97 IC-E