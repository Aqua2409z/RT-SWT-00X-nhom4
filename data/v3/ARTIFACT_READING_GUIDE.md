# Hướng dẫn đọc và kiểm tra artifact Data V3

Tài liệu này mô tả snapshot ngay sau khi Step 003 hoàn thành. Ba file
`ARTIFACT_READING_GUIDE.md`, `build_recipes_portable.csv` và
`BUILD_EVIDENCE_SHA256SUMS.csv` là artifact dẫn xuất; chúng không thay thế và
không sửa lịch sử gốc.

## Trạng thái Step 003 đã niêm phong

- 30 repository qualified, mỗi repository có một exact commit.
- Tất cả baseline build được thực hiện bằng effective JDK 8.
- 678 class đồng thời đạt structural eligibility và thuộc build scope PASS.
- Mỗi repository có 12–71 class như trên.
- 48 winning build scope duy nhất thuộc 30 repository.
- 43 scope Maven và 5 scope Gradle.
- Không winning recipe nào sửa source/build/dependency file.
- Không winning recipe nào dùng ancillary-skip hoặc external-repository fallback.

Marker chính thức là `state/step003.done.json`. Các file mới trong tài liệu này
không được mô tả như output gốc của marker Step 003.

## Thứ tự đọc

| Thứ tự | File | Ý nghĩa |
|---:|---|---|
| 1 | `state/step003.done.json` | Marker PASS, checksum output lõi và tổng số repository/class. |
| 2 | `successful_repos_manifest.csv` | 30 repository cuối, URL, exact commit, số class buildable và vị trí clone. |
| 3 | `build_recipes.jsonl` | 48 winning recipe chính xác như đã chạy trên máy gốc. |
| 4 | `build_recipes_portable.csv` | Bản dẫn xuất có placeholder để replay trên máy khác. |
| 5 | `class_metrics_all.csv` | Metric của mọi focal class thuộc 30 repository cuối. |
| 6 | `excluded_classes_log.csv` | Class bị loại vì eligibility hoặc vì build scope không PASS. |
| 7 | `build_attempts.csv` | Nhật ký mọi attempt, kể cả repo bị loại, retry và resume. |
| 8 | `logs/build/*.log` | stdout/stderr thô của từng build attempt. |
| 9 | `BUILD_EVIDENCE_SHA256SUMS.csv` | SHA-256 dùng để phát hiện artifact/log bị thay đổi sau khi niêm phong. |

## Cách hiểu `build_attempts.csv`

`build_attempts.csv` là audit trail, không phải danh sách build cuối. Snapshot này
có 1.422 attempt thuộc 69 repository đã đi đến bước build; 1.335 attempt failed
và 87 attempt success. Queue đầy đủ có 30 qualified, 68 rejected và 1.150 pending
khi pipeline dừng vì đã đủ 30 repository.

Một scope có thể fail rồi success vì recipe ladder được khai báo trước:

1. thử module trực tiếp;
2. thử biến thể policy đã đăng ký;
3. thử Maven reactor với `-pl <module> -am`, hoặc scope Gradle tương ứng;
4. chỉ khi đúng category cho phép mới thử fallback tiếp theo.

Do đó fail rồi success không có nghĩa pipeline sửa code để ép build. Ví dụ module
Maven có thể không tự resolve được dependency từ module anh em, trong khi reactor
build thành công vì build luôn upstream module.

Một repository cũng có thể có nhiều scope chứa focal classes. Vì vậy 30 repository
có tổng cộng 48 winning scopes.

Hai repository qualified có success row lặp do lịch sử resume:

- `41627638`
- `48046454`

Đây là dòng lịch sử, không phải hai winning scope khác nhau.
`build_recipes.jsonl` và `build_recipes_portable.csv` chỉ có một recipe duy nhất
cho mỗi `repo_id + scope_key`.

## Cách hiểu eligibility

Trong `class_metrics_all.csv`, `eligible_for_sampling=True` mới chỉ nói class đạt
gate cấu trúc. Class thuộc frame cuối khi đồng thời:

```text
eligible_for_sampling=True
build_scope_pass=True
```

Snapshot có 1.307 class đạt gate cấu trúc, nhưng chỉ 678 class đồng thời thuộc
scope build PASS. Step 004 chỉ được lấy mẫu từ giao 678 class này.

## Cấu trúc `build_recipes_portable.csv`

Mỗi dòng tương ứng đúng một dòng winning recipe trong `build_recipes.jsonl`.

| Cột | Ý nghĩa |
|---|---|
| `recipe_id` | ID duy nhất của winning recipe. |
| `repo_url`, `commit_sha` | Nguồn và exact commit phải checkout. |
| `build_tool`, `scope_key` | Build tool và scope đã được chứng minh PASS. |
| `build_root_relative`, `module_dir_relative`, `module_selector` | Vị trí build tương đối trong repository. |
| `working_directory_placeholder` | Luôn là `${REPO_DIR}`. |
| `portable_command_windows` | Template cho Windows. |
| `portable_command_posix` | Template cho Linux/macOS. |
| `validation_log_relative` | Log PASS gốc trong `data_v3`. |
| `validation_log_sha256` | Hash của log PASS gốc. |
| `original_command_sha256` | Hash UTF-8 của command tuyệt đối trong JSONL gốc. |

Portable CSV cố ý không chứa đường dẫn ổ đĩa của máy gốc. `${REPO_DIR}` là token
phải được thay bằng đường dẫn clone trên máy replay; nó không tự động là biến môi
trường của CMD, PowerShell hay Bash.

## Replay một winning recipe trên máy khác

1. Đọc `repo_url` và `commit_sha` từ dòng cần replay.
2. Clone repository cùng submodule và checkout exact commit:

   ```text
   git clone --recurse-submodules <repo_url> <repo_dir>
   git -C <repo_dir> checkout --detach <commit_sha>
   git -C <repo_dir> submodule update --init --recursive
   ```

3. Dùng JDK 8. Đối chiếu Maven, Gradle và môi trường máy gốc trong
   `results/environment_versions.json`, `state/preflight.json` và config snapshot.
4. Chọn `portable_command_windows` hoặc `portable_command_posix`.
5. Thay literal `${REPO_DIR}` bằng đường dẫn tuyệt đối của clone và chạy command
   từ chính repository root.
6. Không sửa source, build file hoặc dependency declaration để đạt PASS.
7. Lưu stdout/stderr, exit code, tool version và thời điểm replay thành evidence mới;
   không ghi đè log gốc.

Trên POSIX, wrapper có thể cần quyền thực thi. Việc cấp quyền thực thi cho wrapper
không được phép thay đổi tracked content; kiểm tra lại bằng `git status --porcelain`.

Các winning command dùng Maven `clean test-compile` hoặc Gradle `testClasses -x test`.
Chúng compile baseline và test sources nhưng không thực thi test suite. Không được
mô tả kết quả này là “toàn bộ unit test PASS”.

Replay có thể thất bại về sau do repository phụ thuộc bên ngoài biến mất hoặc mạng
thay đổi. Khi đó phải báo cáo là replay failure cùng môi trường/thời điểm mới, không
được sửa lịch sử PASS gốc.

## Kiểm tra checksum evidence

Đóng Excel/editor đang mở CSV trước khi kiểm tra. Mở PowerShell tại thư mục
`data_v3` và chạy:

```powershell
$bad = @()
foreach ($row in Import-Csv .\BUILD_EVIDENCE_SHA256SUMS.csv) {
    $path = Join-Path (Get-Location) ($row.path.Replace('/', '\'))
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) {
        $bad += "$($row.path): MISSING"
        continue
    }
    $actual = (Get-FileHash -LiteralPath $path -Algorithm SHA256).Hash.ToLower()
    if ($actual -ne $row.sha256) {
        $bad += "$($row.path): HASH_MISMATCH"
    }
}
if ($bad.Count -eq 0) {
    "PASS: all sealed evidence matches"
} else {
    $bad
}
```

`BUILD_EVIDENCE_SHA256SUMS.csv` bao phủ:

- attempt audit và deterministic repository queue;
- exact và portable winning recipes;
- manifest, class metric frame và excluded-class audit;
- Step 003 marker, config snapshots, amendment và environment evidence;
- toàn bộ build, clone, submodule và pipeline-step log có mặt tại thời điểm niêm phong.

Checksum manifest không tự chứa checksum của chính nó vì điều đó tạo vòng tham
chiếu. Nếu bất kỳ evidence gốc nào thay đổi hoặc có log mới, phải tạo một checksum
manifest phiên bản mới thay vì ghi đè và giả vờ đó là snapshot cũ.

## Kiểm tra tiếp ở Step 004 và Step 005

Step 004 tạo đúng 300 main và 60 backup bằng deterministic seeded hashing. Seed 42
nằm trong chuỗi đầu vào SHA-256; SHA-256 tạo thứ tự ổn định chứ không thay thế seed.
Complexity không tham gia selection.

Step 005 tái dựng selection từ frame, kiểm tra 30 × (10 main + 2 backup), uniqueness,
zero overlap, exact focal paths, effective JDK 8 và chia relative complexity 150/150.
Chỉ khi tất cả PASS mới tạo `results/RUN_READY` cùng các báo cáo:

- `results/validation_report.md`
- `results/sampling_methodology.md`
- `results/data_integrity_report.md`
- `results/SHA256SUMS.csv`

Không bắt đầu GPT/EvoSuite nếu chưa có `RUN_READY`.
