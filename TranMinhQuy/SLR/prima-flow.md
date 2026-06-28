# PRISMA Flow – LLMs for Automated Unit Test Generation

```text==
[Records từ database searching (N = 842)] <- Tổng từ search-log.md
↓
[Sau khi xóa duplicate (N = 552)] <- dòng trong 01_all_records.csv
↓
┌──────────────────────────────────────────────┐
│ Screened title + abstract (N = 552)          │
│  └─ Excluded (N = 488):                      │
│ EC-D=0, EC-A=0, EC-S=1, EC-O=385, EC-N=102   │
└──────────────────────────────────────────────┘
↓ 64 papers pass <- INCLUDE + UNSURE trong 02_v1_screened.csv

┌──────────────────────────────────────────────┐
│ Full-text assessed (N = 64)                  │
│  └─ Excluded (N = 15):                       │
│     EC-O=14, EC-N=1                          │
└──────────────────────────────────────────────┘
↓
[Final included (N = 48)] <- INCLUDE trong 03_final_included.csv

