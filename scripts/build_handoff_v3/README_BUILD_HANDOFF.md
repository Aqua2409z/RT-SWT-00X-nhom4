# Data V3 build handoff

## 1. Phạm vi và nguyên tắc

Thư mục này là công cụ giao nhận độc lập. Nó không phải Step 006 và không sửa
`research_pipeline_v3` hay các artifact đã đóng băng trong `data_v3`.

Runner chỉ tái hiện 48 build recipe thắng của 30 repository cuối. Nó không chạy
JaCoCo, PIT, EvoSuite, không sinh test và không thay đổi tiêu chí lấy mẫu.

Nguồn đóng băng luôn được đọc tại:

```text
data_v3/repos/successful/<repo_id>
```

Mỗi lần build diễn ra trên bản sao writable tại `build_work`. Log và bảng kết
quả mới nằm trong `build_replay_results`. Hai thư mục này không phải bằng chứng
gốc của pipeline.

## 2. Bố cục ZIP bắt buộc

Giải nén ba thư mục sau dưới cùng một thư mục cha:

```text
V3_HANDOFF/
  data_v3/
  research_pipeline_v3/
  v3_build_handoff/
```

Không đổi quan hệ tương đối này nếu vẫn dùng `handoff_config.json` mặc định.
Không cần đóng gói `build_work`, `build_replay_results` hoặc cache cũ.

Gói đầy đủ cần cả `research_pipeline_v3` vì verifier đối chiếu checksum source
inventory của Step 005. Giữ nguyên `.git` bên trong 30 repository để kiểm tra
exact commit và phát hiện tracked-file drift.

Từ thư mục cha `V3_HANDOFF`, ưu tiên TAR.GZ khi giao sang Docker/Linux:

```bat
tar.exe -czf V3_BUILD_HANDOFF.tar.gz data_v3 research_pipeline_v3 v3_build_handoff
certutil -hashfile V3_BUILD_HANDOFF.tar.gz SHA256
```

Nếu team bắt buộc dùng ZIP:

```bat
tar.exe -a -cf V3_BUILD_HANDOFF.zip data_v3 research_pipeline_v3 v3_build_handoff
certutil -hashfile V3_BUILD_HANDOFF.zip SHA256
```

Gửi checksum SHA-256 qua kênh tách biệt với archive. Sau khi giải nén, verifier
mới là kiểm tra cấp file; checksum archive chỉ chứng minh file chuyển giao không
bị đổi trên đường truyền.

## 3. Cấu hình tương đối

`handoff_config.json` không chứa đường dẫn của máy tạo dữ liệu. Mọi đường dẫn
được giải tương đối từ chính file cấu hình:

```text
data_root   = ../data_v3
work_root   = ../build_work
result_root = ../build_replay_results
```

Trong recipe, `${REPO_DIR}` là placeholder trung lập, không phải cú pháp để
người dùng gõ trực tiếp trong CMD. Runner thay placeholder bằng đường dẫn
workspace thực rồi chọn cột lệnh Windows hoặc POSIX. Với 7 recipe có
`build_root_relative` khác `.`, runner gắn thêm đường dẫn build scope đã ghi
trong cùng dòng CSV. Việc này tái tạo working directory/lệnh gốc mà không sửa
artifact portable đã niêm phong.

Ví dụ recipe:

```text
v3:46450575:maven:contrib/flo-bigquery
```

sẽ được runner biến thành lệnh Maven có `-f` trỏ đến bản sao writable của repo
`46450575`. Không cần tự sửa CSV và không copy đường dẫn `D:\material\...`.
Build luôn dùng bản sao dưới `build_work`, không chạy Maven/Gradle trực tiếp
trên `data_v3`.

## 4. Kiểm tra gói không build

Từ thư mục `v3_build_handoff`:

```bat
py scripts\verify_bundle.py --config handoff_config.json
py scripts\verify_bundle.py --config handoff_config.json --full-checksums
```

Lệnh đầu kiểm tra cấu trúc, RUN_READY, source inventory Step 005, 30 commit,
300 main, 60 backup, 10+2 mỗi repo, không overlap, 48 recipe và log bằng chứng.
`--full-checksums` băm lại toàn bộ inventory build evidence nên chậm hơn.

`verify_bundle.py` và `replay_builds.py --check-only` không tạo workspace, không
chạy Maven/Gradle và không ghi summary.

## 5. Chạy native Windows

Yêu cầu:

- Python 3.9 trở lên;
- JDK đầy đủ 8u172 hoặc JDK 8 tương thích, có cả `java` và `javac`;
- Maven 3.9.15 tại đường dẫn đã cấu hình;
- Git;
- mạng hoặc dependency cache cho các dependency/wrapper chưa có.

Máy tạo dữ liệu hiện dùng mặc định:

```text
C:\Program Files\Java\jdk1.8.0_172
C:\Program Files\apache-maven-3.9.15
```

Mở **CMD mới**, sau đó:

```bat
cd /d "D:\duong-dan\V3_HANDOFF\v3_build_handoff"
call native_windows\setup_jdk8.cmd
native_windows\preflight.cmd
```

Nếu cài ở nơi khác:

```bat
set "JDK8_HOME=C:\Tools\Java\jdk8"
set "MAVEN_3915_HOME=C:\Tools\apache-maven-3.9.15"
call native_windows\setup_jdk8.cmd
```

Không trỏ `JDK8_HOME` tới `jre1.8.0_172`: JRE có `java` nhưng không có `javac`.
Sau setup, `mvn -version` phải báo Java 8 từ JDK vừa chọn.

Preflight toàn bộ recipe nhưng không build:

```bat
py scripts\replay_builds.py --config handoff_config.json --check-only
```

Build một repository:

```bat
py scripts\replay_builds.py --config handoff_config.json --repo-id 46450575
```

Build đúng một recipe:

```bat
py scripts\replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

Build toàn bộ và tiếp tục an toàn sau khi gián đoạn:

```bat
py scripts\replay_builds.py --config handoff_config.json --all --resume --jobs 1
```

`--resume` chỉ bỏ qua một PASS cũ khi cả hash lệnh gốc và fingerprint môi trường
khớp. Một PASS từ môi trường khác không bị coi là kết quả thay thế hợp lệ.

## 6. Chạy bằng Docker

### 6.1 Vai trò của image V2

Dockerfile dùng image V2 làm **base toolchain JDK 8**, không chạy script hoặc
config V2. Nó cài đè Maven toàn cục bằng đúng 3.9.15. Các recipe Gradle cuối đều
dùng wrapper trong repository; runner không dùng Gradle toàn cục của image V2.

Mặc định:

```text
minhquy266/classes2test-pipeline:pilot-v1
```

Nếu image V2 được nạp từ TAR với tag khác, đặt biến trước khi build:

```bat
set "V2_BASE_IMAGE=ten-image-v2:tag"
docker compose build
```

### 6.2 Build và preflight

Khởi động Docker Desktop trước, rồi từ `v3_build_handoff`:

```bat
docker compose build
docker compose run --rm build-handoff
```

Lệnh mặc định là `--check-only`. Compose mount:

- `data_v3` và `research_pipeline_v3` read-only;
- `build_work`, `build_replay_results`, Maven cache và Gradle cache read-write.

Build một recipe mẫu:

```bat
docker compose run --rm build-handoff python3 scripts/replay_builds.py --config handoff_config.json --recipe-id v3:46450575:maven:contrib/flo-bigquery
```

Replay toàn bộ:

```bat
docker compose run --rm build-handoff python3 scripts/replay_builds.py --config handoff_config.json --all --resume --jobs 1
```

Giữ `--jobs 1` trong lần xác nhận đầu tiên. Chỉ tăng song song sau khi đo RAM,
vì một số Maven reactor/Gradle daemon dùng nhiều bộ nhớ. Runner song song theo
repository, không chạy đồng thời hai scope trong cùng một repo.

JDK trong container là Temurin JDK 8 trên Linux, trong khi lượt thu thập gốc dùng
Oracle JDK 8u172 trên Windows. Vì vậy Docker là môi trường portability, không
được tuyên bố là tái hiện thành công cho đến khi đạt 48/48 PASS.

## 7. Output và cách đọc

Mỗi attempt mới tạo:

```text
build_replay_results/
  environment.json
  build_replay_summary.csv
  logs/<repo_id>/<recipe-id>.stdout.log
  logs/<repo_id>/<recipe-id>.stderr.log
```

`build_replay_summary.csv` ghi recipe, commit, lệnh đã resolve, thời gian, exit
code, failure category, tracked changes, đường dẫn log và fingerprint môi trường.
File này là audit append-only: khi resume một FAIL, attempt mới được thêm thay vì
xóa lịch sử cũ. Khi tính 48/48, lấy attempt mới nhất của từng `recipe_id` trong
cùng fingerprint môi trường.

`resolved_command` và đường dẫn log trong summary cố ý là đường dẫn tuyệt đối của
máy đã chạy để làm bằng chứng. Không đưa chúng cho máy khác như recipe; máy khác
luôn đọc lại `build_recipes_portable.csv` qua runner.

Thư mục `v3_build_handoff/output` được chừa cho checksum/archive note của đợt
bàn giao. Runner không đặt build log ở đó nhằm giữ tách biệt output giao nhận với
`build_replay_results`.

PASS hợp lệ cần đồng thời:

- process exit code bằng 0;
- HEAD đúng `commit_sha`;
- không có tracked file bị sửa sau build.

`test-compile` và `testClasses -x test` chỉ biên dịch baseline/main/test source;
chúng không chạy toàn bộ test suite của repository.

Nếu dependency server, DNS, TLS hoặc wrapper download lỗi, giữ nguyên log và coi
đó là lỗi replay/môi trường. Không đổi dependency, build file, commit hoặc recipe
để ép PASS.

## 8. Quy trình chấp nhận của team

1. Xác minh SHA-256 của file ZIP/TAR do người giao cung cấp.
2. Giải nén đúng bố cục ba thư mục.
3. Chạy `verify_bundle.py --full-checksums`.
4. Chạy `replay_builds.py --check-only`.
5. Smoke-test recipe `v3:46450575:maven:contrib/flo-bigquery`.
6. Chạy `--all --resume --jobs 1`.
7. Chỉ chấp nhận môi trường khi summary mới có 48/48 PASS, 30/30 exact commit và
   `tracked_changes` trống.
8. Niêm phong image bằng digest và lưu checksum riêng cho archive, cache (nếu
   giao cache), summary và log mới.

Không sửa `results/SHA256SUMS.csv`, marker Step 001–005 hoặc checksum bằng chứng
gốc để thêm kết quả handoff. Kết quả replay là lớp bằng chứng mới, tách biệt.

## 9. Dung lượng và cache

Bản sao workspace có thể cần thêm vài GB vì `data_v3/repos/successful` hiện lớn
khoảng 3.55 GB. Cache Maven/Gradle giúp replay ổn định hơn nhưng phải được tạo từ
lượt replay này và niêm phong riêng; không nên giao toàn bộ cache toàn cục lẫn
artifact không liên quan.

Sau khi team đã lưu log cần thiết, có thể xóa riêng `build_work` để thu hồi dung
lượng. Không xóa hoặc clean trực tiếp `data_v3/repos/successful`.
