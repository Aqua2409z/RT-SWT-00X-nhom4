# PRISMA Flow - LLM cho sinh unit test

```text
[Records từ tìm kiếm cơ sở dữ liệu (N = 147)]
		  ↓
[Sau khi loại trùng lặp (N = 120)]
		  ↓
┌──────────────────────────────────────────┐
│ Sàng lọc tiêu đề + abstract (N = 120)    │
│    └── Loại ở V1 (N = 71)                │
└──────────────────────────────────────────┘
		  ↓
[Đánh giá toàn văn (N = 49)]
		  ↓
┌──────────────────────────────────────────────────────────────────────┐
│ Đánh giá toàn văn (N = 49)                                           │
│    └── Loại ở V2: 33                                                 │
│    └── Chờ PDF / Pending V2 (N = 0)                                 │
│    └── Cuối cùng được đưa vào (N = 16)                               │
└──────────────────────────────────────────────────────────────────────┘
		  ↓
[Cuối cùng được đưa vào (N = 16)]
```

## Kiểm tra tính nhất quán

| Kiểm tra                                              | Kỳ vọng | Thực tế | Trạng thái |
| ----------------------------------------------------- | ------: | ------: | ---------- |
| Số dòng trong 01_all_records.csv = sau loại trùng     |     120 |     120 | PASS       |
| Số dòng trong 02_after_screening_v1.csv = đã sàng lọc |     120 |     120 | PASS       |
| Số V1 EXCLUDE = loại ở PRISMA                         |      71 |      71 | PASS       |
| Số V1 INCLUDE + Unsure = đã đánh giá toàn văn         |      49 |      49 | PASS       |
| Số V2 Include = cuối cùng được đưa vào                |      16 |      16 | PASS       |
| Số V2 Exclude = loại ở V2                             |      33 |      33 | PASS       |
| Số V2 Pending = đang chờ PDF                          |       0 |       0 | PASS       |

## Ghi chú

- Quy trình đã chốt V2 ở thời điểm rà soát cuối: các bài không tìm được PDF/full-text được gán `EC-A` và `V2 = Exclude`.
- 21 PDF đã được quét trong `SLR/papers/`; trong đó 16 bài là `V2 Include`.
- Danh sách 28 bài paywalled/không tải được full-text đã được ghi rõ trong `search-log.md`.
