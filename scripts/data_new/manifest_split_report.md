# Manifest Split Report

- Created UTC: `2026-07-23T11:07:03+00:00`
- Source manifest: `D:\A_ThucNghiem\scripts_v2\data_new\class_sampling_manifest_final_seed42.csv`
- Source SHA-256: `904a53dc6ed7741c91765cd2ca3d38929a10f8b34d10d6019b8175b5f514af3b`
- Algorithm: `deterministic split from already sampled seed42 manifest`
- Pilot policy: `2 classes per repo; prefer one lower_complexity_half and one higher_complexity_half`

## Summary

| Split | Rows | Repos | Min class/repo | Max class/repo | Scopes | Duplicate class_key rows | Duplicate focal rows |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| pilot60 | 60 | 30 | 2 | 2 | 38 | 0 | 0 |
| remaining240 | 240 | 30 | 8 | 8 | 47 | 0 | 0 |

## Complexity Half Counts

### pilot60
- `higher_complexity_half`: 31
- `lower_complexity_half`: 29

### remaining240
- `higher_complexity_half`: 119
- `lower_complexity_half`: 121

## Notes

- Pilot lấy đúng `2` class mỗi repo.
- Khi repo có cả `lower_complexity_half` và `higher_complexity_half`, pilot lấy một class ở mỗi nửa theo `selection_rank_in_repo` rồi `selection_hash`.
- Nếu repo thiếu một nửa complexity, pilot lấy thêm class kế tiếp theo thứ tự deterministic đã khóa.
- Remaining chứa toàn bộ class còn lại và không trùng `class_key` với pilot.
