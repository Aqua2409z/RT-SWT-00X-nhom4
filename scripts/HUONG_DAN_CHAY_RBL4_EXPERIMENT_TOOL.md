# Hướng Dẫn Chạy RBL-4 Experiment Tool

Tài liệu này hướng dẫn cách chạy giao diện React và backend Python cho tool thực nghiệm RBL-4 trong thư mục `data2/class2test`.

Tool này dùng để:

- Chọn mẫu thực nghiệm `pilot_60_classes.csv`, `classes_main.csv`, `classes_part1.csv`, `classes_part2.csv`, `classes_part3.csv`, hoặc `pilot_24_classes.csv`.
- Chạy pipeline sinh test class bằng GPT-4o-mini và EvoSuite 1.2.0.
- Đo kết quả bằng JaCoCo và PIT thông qua AgoneTest.
- Theo dõi tiến trình realtime trên giao diện React.
- Xuất báo cáo `.csv` và `.xlsx`.

## 1. Vị trí thư mục

Từ thư mục gốc repository, đi vào:

```powershell
cd "D:\FPT University\SUBJECT - SUMMER 26\SWT301\SE196508_TruongDanHuy\data2\class2test"
```

Các phần chính:

- `experiment_tool/`: backend FastAPI và runner Python.
- `react-ui/`: giao diện React.
- `AgoneTest/`: framework AgoneTest gốc.
- `output/`: file mẫu thực nghiệm và một số output AgoneTest.
- `repos/`: mã nguồn Java đã clone/copy theo từng `repo_id`.
- `compiledrepos/`: working tree mà AgoneTest cũ sử dụng; nếu thiếu, runner tạo alias/junction trỏ về `repos/`.
- `results/runs/`: nơi lưu kết quả các lần chạy mới.

## 2. Chuẩn bị môi trường

### 2.1. Python

Cài dependency của AgoneTest và backend:

```powershell
pip install -r AgoneTest/requirements.txt
pip install -r experiment_tool/requirements.txt
```

Nếu máy dùng lệnh `python -m pip` thay vì `pip`, chạy:

```powershell
python -m pip install -r AgoneTest/requirements.txt
python -m pip install -r experiment_tool/requirements.txt
```

### 2.2. Node.js

Đi vào thư mục React:

```powershell
cd react-ui
npm install
```

Sau đó quay lại thư mục `class2test` nếu cần:

```powershell
cd ..
```

### 2.3. File `.env`

File `.env` nằm trong `data2/class2test`.

Cần kiểm tra các biến sau:

```env
OPENAI_API_KEY=...
JAVA_HOME_8=...
JAVA_HOME_11=...
JAVA_HOME_17=...
JAVA_HOME_21=...
JAVA_HOME_DEFAULT=...
```

Lưu ý:

- `OPENAI_API_KEY` chỉ cần khi chạy `Full Run`.
- `Dry Run` không gọi OpenAI, EvoSuite, JaCoCo hoặc PIT.
- Không đưa file `.env` lên Git hoặc nộp kèm báo cáo.

### 2.4. Tạo file Pilot 60 và Full 300 từ manifest

Từ thư mục gốc bundle, chạy:

```powershell
python scripts/create_rbl4_samples.py
```

Script này đọc `class_sampling_manifest_seed42.csv` và ghi ra:

- `output/pilot_60_classes.csv`: 60 class pilot, phủ đủ 33 repo, không trùng cặp `repo_id + focal_path + test_path`.
- `output/classes_main.csv`: 300 dòng main để chạy full.
- `output/classes_backup.csv`: 58 dòng backup theo manifest.
- `output/pilot_60_backups.csv`: 47 backup unique để đối chiếu/thay thế thủ công nếu cần.
- `output/classes_part1.csv`, `output/classes_part2.csv`, `output/classes_part3.csv`: 3 chunk 100 dòng.
- `metadata/pilot_60_selection_report.json`: audit phân phối repo/CC.

Lưu ý dữ liệu: manifest hiện có 300 dòng main nhưng chỉ có 103 cặp unique `repo_id + focal_path + test_path`. Pilot 60 cố ý lấy unique class/test pair để tránh chạy lặp cùng một class trong bước pilot. Full 300 vẫn giữ đủ 300 dòng main theo manifest.

### 2.5. Kiểm tra dữ liệu trong container

Trong container, tại thư mục `/pilot` hoặc thư mục gốc bundle tương ứng, chạy:

```bash
python3 -c "import pandas as pd; from pathlib import Path; d=pd.read_csv('class_sampling_manifest_seed42.csv',dtype={'repo_id':str}); expected=set(d.repo_id); actual={p.name for p in Path('repos').iterdir() if p.is_dir()}; print('CSV REPOS:',len(expected)); print('SOURCE REPOS:',len(actual)); print('MISSING:',sorted(expected-actual)); print('EXTRA:',sorted(actual-expected))"
```

Kết quả đúng:

```text
CSV REPOS: 33
SOURCE REPOS: 33
MISSING: []
EXTRA: []
```

Runner sẽ tự tạo `compiledrepos -> repos` khi thiếu `compiledrepos` để tương thích với AgoneTest cũ. Cách này không copy thêm dữ liệu và phù hợp cho `Dry Run`. Với `Full Run`, AgoneTest có thể sửa test/POM tạm thời rồi restore; nếu muốn giữ `repos/` tuyệt đối nguyên bản, hãy tạo bản copy `compiledrepos/` riêng trước khi chạy full.

### 2.6. Tối ưu baseline build

Baseline build không còn chạy `mvn install` root đại trà cho mọi repo multi-module. Runner chạy module trước; nếu module fail vì thiếu artifact nội bộ `*-SNAPSHOT`, runner mới prepare reactor một lần cho repo đó bằng `-pl <module> -am` rồi retry.

Mặc định baseline chạy song song theo repo với 2 worker:

```powershell
$env:RBL4_BASELINE_MAX_WORKERS="2"
```

Nếu máy yếu hoặc Docker thiếu RAM, giảm về 1:

```powershell
$env:RBL4_BASELINE_MAX_WORKERS="1"
```

Các repair đặc biệt hiện có:

- `repair_configs/37728390/local_maven_repair.json`: install jar legacy local và giới hạn compile vào package sampled của `geomason`.
- `repair_configs/58767125/local_gradle_repair.json`: disable Dokka 0.9.15 classpath cũ để Gradle baseline `testClasses` chạy được.

## 3. Chạy backend FastAPI

Mở một terminal tại:

```powershell
cd "D:\FPT University\SUBJECT - SUMMER 26\SWT301\SE196508_TruongDanHuy\data2\class2test"
```

Chạy backend:

```powershell
python -m uvicorn experiment_tool.app:app --host 127.0.0.1 --port 8000
```

Nếu chạy thành công, terminal sẽ hiện:

```text
Uvicorn running on http://127.0.0.1:8000
```

Có thể kiểm tra backend bằng trình duyệt:

- API health: `http://127.0.0.1:8000/api/health`
- API docs: `http://127.0.0.1:8000/docs`

## 4. Chạy giao diện React

Mở terminal thứ hai:

```powershell
cd "D:\FPT University\SUBJECT - SUMMER 26\SWT301\SE196508_TruongDanHuy\data2\class2test\react-ui"
```

Chạy giao diện:

```powershell
npm run dev -- --port 5173 --strictPort
```

Nếu port `5173` đang bận, có thể dùng:

```powershell
npm run dev -- --port 5174
```

Mở trình duyệt:

```text
http://127.0.0.1:5173
```

Giao diện sẽ tự gọi backend qua `/api`.

## 5. Cách sử dụng giao diện

### 5.1. Chọn mẫu chạy

Trong khung `Run Launcher`, chọn `Sample`:

- `Pilot 60`: dùng `output/pilot_60_classes.csv`.
- `Pilot Part 1`: dùng `output/classes_part1.csv`.
- `Pilot Part 2`: dùng `output/classes_part2.csv`.
- `Pilot Part 3`: dùng `output/classes_part3.csv`.
- `Pilot 24 New`: dùng `output/pilot_24_classes.csv`.
- `Full 300`: dùng `output/classes_main.csv`.
- `Custom CSV`: nhập đường dẫn CSV tùy chọn nằm trong `data2/class2test`.

Khuyến nghị:

- Chạy `Pilot 60` với `Dry Run` trước để kiểm tra tool.
- Chạy `Pilot 60` với `Full Run` trước khi chạy `Full 300`.
- Chỉ chạy `Full 300` khi môi trường Java, Maven, OpenAI key và repo build đã ổn định.

### 5.2. Chọn chế độ chạy

Tool có 3 chế độ:

#### Dry Run

Không gọi OpenAI, EvoSuite, JaCoCo hoặc PIT.

Dùng để kiểm tra:

- Backend có chạy không.
- React có kết nối API không.
- File sample có đọc được không.
- Precheck có nhận diện class buildable không.
- Report CSV/XLSX có xuất được không.

Đây là chế độ nên chạy đầu tiên.

#### Report Only

Không chạy lại GPT/EvoSuite.

Dùng để:

- Đọc output AgoneTest đã có sẵn.
- Tạo lại `metrics_long.csv`, `summary.csv`, `generated_failures.csv`, `experiment_report.xlsx`.

#### Full Run

Chạy pipeline đầy đủ:

1. Precheck class buildable.
2. Baseline build repo/module trước khi sinh test.
3. Sinh test bằng EvoSuite 1.2.0.
4. Sinh test bằng GPT-4o-mini.
5. Compile test class.
6. Đo coverage bằng JaCoCo.
7. Đo mutation bằng PIT.
8. Xuất CSV và Excel.

Lưu ý:

- Chế độ này có thể chạy lâu.
- Có gọi OpenAI API.
- Có thể tốn chi phí API.
- Nên chạy pilot nhỏ trước khi chạy full 300.

### 5.3. Model và Prompt

Giá trị mặc định:

```text
Model:  gpt-4o-mini-2024-07-18
Prompt: rbl4-zero-shot
```

Không nên đổi prompt cho RQ1-RQ3 vì `proposal.md` đã khóa protocol zero-shot để so sánh với AgoneTest-compatible scale.

### 5.4. Chạy file pilot 24 mới

File mới đã được chuẩn hóa tại:

```text
D:\pilot_delivery\pilot_delivery\pilot_bundle_24\output\pilot_24_classes.csv
```

File này được tạo từ:

```text
d:\pilot_delivery\pilot_delivery\pilot_bundle_24\pilot_24_classes.csv
```

Tool không tự copy repo khi chọn `Pilot 24 New`. Trước khi chạy thật, cần tự copy các repo tương ứng vào:

```text
D:\pilot_delivery\pilot_delivery\pilot_bundle_24\compiledrepos\<repo_id>
```

Ví dụ:

```powershell
robocopy "D:\pilot_delivery\pilot_delivery\pilot_bundle_24\compiledrepos" "D:\path\to\your\class2test\compiledrepos" /E
```

CSV pilot 24 có thêm metadata `Java_Version` và `Build_Tool`. Runner sẽ dùng metadata này để sinh `output/project_info.json`, giúp tự chọn JDK và build tool phù hợp hơn cho từng project/module.

Khi metadata CSV và build file thật không khớp, runner ưu tiên build file tại repo/module (`pom.xml`, `build.gradle`, `build.gradle.kts`) để tránh chạy nhầm Maven/Gradle. Ví dụ module có `build.gradle` sẽ được xử lý như Gradle dù cột `Build_Tool` trong CSV ghi `maven`.

Nếu chạy trong chính bundle `D:\pilot_delivery\pilot_delivery\pilot_bundle_24` thì repo đã nằm sẵn ở `compiledrepos/`; không cần copy thêm. Lệnh `robocopy` chỉ cần dùng khi muốn chuyển bundle sang một thư mục tool khác.

## 6. Theo dõi realtime

Giao diện hiển thị pipeline theo các bước:

1. `Precheck`
2. `Baseline`
3. `Generate`
4. `Compile`
5. `JaCoCo`
6. `PIT`
7. `Report`

Trong `Full Run`, bước `Baseline` chạy build/test-compile sạch trên từng repo/module trước khi gọi GPT hoặc EvoSuite. Chỉ các class thuộc repo/module baseline `PASS` mới được đưa sang bước sinh test. Nếu baseline `FAIL`, class đó được ghi vào report với `fail_stage=repo_baseline_failed`; lỗi này là lỗi build sẵn của repo/module, không tính là lỗi test class do GPT/EvoSuite sinh ra.

Khu vực `Live Log` hiển thị event realtime từ backend qua SSE.

Có thể lọc log theo:

- project id
- arm `gpt` hoặc `evosuite`
- focal class
- trạng thái `START`, `PASS`, `SKIP`, `ERROR`
- nội dung lỗi

## 7. Kết quả đầu ra

Mỗi lần chạy tạo một thư mục riêng:

```text
results/runs/{run_id}/
```

Các file quan trọng:

- `metrics_long.csv`: kết quả chi tiết từng class và từng arm.
- `summary.csv`: tổng hợp theo arm GPT/EvoSuite.
- `generated_failures.csv`: các case lỗi hoặc không compile.
- `baseline_build.csv`: kết quả build/test-compile sạch trước khi sinh test, theo từng project/module.
- `generation_classes.csv`: danh sách class thực sự được đưa vào AgoneTest sau baseline gate.
- `generated_tests_manifest.csv`: danh sách các file test class `.java` đã sinh ra và nơi lưu bản sao trong run.
- `generated_tests.zip`: file nén chứa toàn bộ test class `.java` sinh ra trong run.
- `error_summary.csv`: bảng lỗi đã được phân loại, có giải thích và gợi ý xử lý bằng tiếng Việt.
- `skipped_classes.csv`: class bị skip ở bước precheck.
- `phase_log.csv`: log realtime theo từng phase.
- `api_log.csv`: log gọi model, token và trạng thái API nếu có chạy GPT.
- `runtime_errors.csv`: lỗi runtime khi chạy AgoneTest.
- `environment_checks.csv`: kiểm tra môi trường.
- `manifest.json`: metadata của run.
- `experiment_report.xlsx`: file Excel tổng hợp nhiều sheet.

Trong giao diện React:

- Panel `Artifacts` dùng để tải CSV/XLSX/log/zip.
- Ô `Baseline OK`, `Baseline Fail`, `Generation` cho biết số class qua baseline và số class thật sự được sinh test.
- Panel `Diagnostics` hiển thị lỗi đã phân loại và hướng xử lý.
- Panel `GPT API Calls` hiển thị số lần gọi model, token, thời lượng và trạng thái OK/ERROR.
- Panel `Generated Test Classes` hiển thị trực tiếp các file test `.java` đã sinh, gồm project, arm GPT/EvoSuite và đường dẫn bản sao theo run.

## 8. Test class sinh ra được lưu ở đâu?

Có 2 nơi cần phân biệt.

### 8.1. Nơi AgoneTest ghi file gốc

Trong lúc chạy, AgoneTest ghi test class sinh ra vào:

```text
output/{project_id}/response_*.java
```

Ví dụ GPT:

```text
output/42324543/response_gpt-4o-mini-2024-07-18_rbl4-zero-shot_RetransmitterTest.java
```

Ví dụ EvoSuite:

```text
output/{project_id}/response_evosuite_{TestClass}.java
```

Lưu ý:

- Thư mục `output/` là vùng làm việc chung của AgoneTest.
- Khi chạy nhiều lần, file trong `output/` có thể bị ghi đè hoặc dọn theo cấu hình run.

### 8.2. Nơi tool lưu bản sao theo từng run

Sau mỗi `Full Run` hoặc `Report Only`, tool sẽ gom test class vào thư mục riêng của run:

```text
results/runs/{run_id}/generated_tests/{project_id}/
```

Đồng thời tool tạo:

```text
results/runs/{run_id}/generated_tests_manifest.csv
results/runs/{run_id}/generated_tests.zip
```

Nên dùng `generated_tests.zip` hoặc `generated_tests_manifest.csv` khi viết báo cáo hoặc đối chiếu kết quả, vì đây là bản đã được đóng gói theo đúng `run_id`.

Trên giao diện React, panel `Generated Test Classes` đọc từ `generated_tests_manifest.csv` và hiển thị nhanh các file `.java` đã được gom vào thư mục `generated_tests/`.

## 9. Log lỗi được ghi ở đâu?

Các lỗi được ghi theo nhiều mức để dễ debug.

### 9.1. Log realtime theo phase

File:

```text
results/runs/{run_id}/phase_log.csv
```

File này ghi từng bước:

- `precheck`
- `baseline_build`
- `stage`
- `generation`
- `evosuite_generate`
- `maven_verify_jacoco_pit`
- `project_run`
- `report`

Mỗi dòng có:

- thời điểm
- phase
- project
- module
- arm
- focal class
- status
- detail lỗi

Đây là file quan trọng nhất để xem lỗi xảy ra ở bước nào.

### 9.2. Bảng lỗi đã phân loại

File:

```text
results/runs/{run_id}/error_summary.csv
```

File này đọc từ `phase_log.csv` và `runtime_errors.csv`, sau đó phân loại lỗi thành các nhóm như:

- `repo_baseline_build_failed`
- `evosuite_maven_plugin_unresolved`
- `test_smell_output_path_invalid`
- `project_dependency_resolution_error`
- `java_source_level_incompatible`
- `gradle_build_failed`
- `java_runtime_too_old_for_maven`
- `pom_rewrite_namespace_error`
- `maven_plugin_management_policy_failure`
- `generated_test_assertion_failure`
- `test_framework_dependency_missing`
- `project_compilation_error`
- `metric_failure_without_parser_detail`
- `llm_api_error`
- `unclassified_runtime_issue`

Các cột quan trọng:

- `category`: loại lỗi.
- `severity`: mức độ.
- `explanation_vi`: giải thích bằng tiếng Việt.
- `suggested_action_vi`: hướng xử lý đề xuất.
- `detail`: log gốc rút gọn.

Trên giao diện React, các lỗi này hiện ở panel `Diagnostics`.

### 9.3. Runtime errors

File:

```text
results/runs/{run_id}/runtime_errors.csv
```

File này ghi exception Python khi AgoneTest hoặc runner bị lỗi.

Ví dụ:

```text
FileNotFoundError: [WinError 3] ...
```

### 9.4. Log gọi model

File:

```text
results/runs/{run_id}/api_log.csv
```

File này ghi:

- model được gọi
- thời lượng gọi
- số token
- trạng thái OK/ERROR
- error message nếu API lỗi

File này không ghi `OPENAI_API_KEY`.

## 10. Giải thích một số lỗi thường gặp trong Full Run

### 10.1. Repo/module fail baseline build

Ví dụ log:

```text
baseline_build,FAIL,...
repo_baseline_failed
```

Ý nghĩa:

- Repo/module không build hoặc test-compile sạch trước khi sinh test.
- Tool sẽ không gọi GPT/EvoSuite cho các class thuộc repo/module này.
- Trong `metrics_long.csv`, cả hai arm sẽ có `fail_stage=repo_baseline_failed` và `baseline_build_status=FAIL`.
- Lỗi này là lỗi build sẵn của repo/module, không phải lỗi do GPT hoặc EvoSuite tạo test sai.

Hướng xử lý:

- Mở `baseline_build.csv` để xem `command`, `build_path`, `java_version`, `detail`.
- Sửa dependency, Maven/Gradle wrapper, profile hoặc JDK của repo/module.
- Chạy lại `Full Run`; chỉ class baseline `PASS` mới đi tiếp sang generation.

### 10.2. EvoSuite Maven plugin không resolve được

Ví dụ log:

```text
Plugin org.evosuite.plugins:evosuite-maven-plugin:1.2.0 or one of its dependencies could not be resolved
org.evosuite.plugins:evosuite-maven-plugin:jar:1.2.0 was not found in https://repo.maven.apache.org/maven2
```

Ý nghĩa:

- Maven đang cố tải `org.evosuite.plugins:evosuite-maven-plugin:1.2.0`.
- Artifact này không resolve được từ Maven Central.
- Maven còn cache lần fail trước trong `~/.m2`, nên có thể không thử tải lại cho đến khi hết update interval hoặc bị force update.

Đây là lỗi ở bước `evosuite_generate`, không phải lỗi GPT.

Hướng xử lý:

- Ưu tiên dùng EvoSuite CLI jar local `evosuite-1.2.0.jar`.
- Nếu vẫn dùng Maven plugin thì cần cấu hình repository/plugin đúng và chạy Maven với `-U`.
- Có thể xóa cache artifact lỗi trong thư mục `.m2/repository/org/evosuite`.

### 10.3. Project thiếu dependency nội bộ hoặc SNAPSHOT

Ví dụ log:

```text
Could not resolve dependencies
Could not find artifact ...:...:jar:...-SNAPSHOT
```

Ý nghĩa:

- Project là multi-module hoặc phụ thuộc artifact nội bộ.
- Module đang được đo riêng nên Maven không tìm thấy các module/artifact cần thiết trong local repository.

Hướng xử lý:

- Build/install từ root project trước khi đo module, ví dụ `mvn install -DskipTests`.
- Nếu dependency không thể khôi phục, nên ghi nhận instance đó fail hoặc loại khỏi pilot sample.

### 10.4. Java source level không tương thích

Ví dụ log:

```text
diamond operator is not supported in -source 1.6
```

Ý nghĩa:

- Test hoặc source dùng cú pháp Java mới hơn nhưng project đang compile với source level cũ.
- Đây thường là lỗi compile khi đo JaCoCo/PIT, không phải lỗi gọi GPT.

Hướng xử lý:

- Chạy đúng JDK/source level theo project.
- Với thực nghiệm fairness, không sửa test sinh ra sau khi sinh; ghi nhận fail stage để báo cáo.

### 10.5. Gradle build failed

Ví dụ log:

```text
BUILD FAILED in 27s
```

Ý nghĩa:

- Gradle fail trong bước test/JaCoCo/PIT nhưng log rút gọn chưa đủ nguyên nhân.

Hướng xử lý:

- Chạy lại lệnh Gradle với `--stacktrace` hoặc `--info`.
- Kiểm tra dependency, plugin, JDK và test runtime của module đó.

### 10.6. TestSmellDetector báo FileNotFoundError khi module có dấu `/`

Ví dụ log:

```text
FileNotFoundError: ... Output_TestSmellDetection_*.csv -> TestSmellDetection_38743792_odps-sdk-impl/odps-mapred-bridge_...
```

Ý nghĩa:

- Module tên `odps-sdk-impl/odps-mapred-bridge` có dấu `/`.
- Khi đặt tên file CSV, Windows hiểu dấu `/` là thư mục con.
- Vì thư mục con không tồn tại nên `os.rename` lỗi.

Tool đã được cập nhật để tự sanitize tên module trước khi đặt tên file `TestSmellDetection_*.csv`.

### 10.7. Project compilation error

Ví dụ log:

```text
cannot find symbol
symbol: class StsAccount
```

Ý nghĩa:

- Source hoặc dependency của project/module không compile được khi chạy đo JaCoCo/PIT.
- Đây là lỗi buildability của repo hoặc module tại thời điểm đo metric.
- Không nhất thiết là lỗi của test class sinh bởi GPT.

Hướng xử lý:

- Mở `phase_log.csv`.
- Xem project/module/focal class bị lỗi.
- Kiểm tra dependency, Java version, Maven profile hoặc loại instance khỏi sample nếu không tái lập được build.

## 11. Chạy bằng API không qua giao diện

Ví dụ tạo một `Dry Run` bằng PowerShell:

```powershell
$body = @{
  sample_key = "part1"
  run_mode = "dry_run"
  model = "gpt-4o-mini-2024-07-18"
  prompt = "rbl4-zero-shot"
  resume = $false
  clear_agone_output = $true
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri http://127.0.0.1:8000/api/runs `
  -ContentType "application/json" `
  -Body $body
```

Xem danh sách run:

```powershell
Invoke-RestMethod http://127.0.0.1:8000/api/runs
```

Xem chi tiết một run:

```powershell
Invoke-RestMethod http://127.0.0.1:8000/api/runs/{run_id}
```

## 12. Chạy worker trực tiếp không qua backend

Có thể chạy worker Python trực tiếp:

```powershell
python -m experiment_tool.runner `
  --run-id manual_dry_run `
  --sample-csv output/classes_part1.csv `
  --mode dry_run `
  --model gpt-4o-mini-2024-07-18 `
  --prompt rbl4-zero-shot `
  --clear-agone-output
```

Kết quả sẽ nằm tại:

```text
results/runs/manual_dry_run/
```

## 13. Kiểm tra build và test giao diện

Trong thư mục `react-ui`:

```powershell
npm run test
npm run build
npm audit
```

Kỳ vọng:

- Test pass.
- Build pass.
- `npm audit` báo `0 vulnerabilities`.

## 14. Lỗi thường gặp

### Backend không chạy vì thiếu FastAPI

Chạy:

```powershell
pip install -r experiment_tool/requirements.txt
```

### React không chạy vì thiếu package

Chạy:

```powershell
cd react-ui
npm install
```

### Port 8000 bị bận

Chạy backend ở port khác:

```powershell
python -m uvicorn experiment_tool.app:app --host 127.0.0.1 --port 8001
```

Sau đó tạo file `react-ui/.env`:

```env
VITE_API_BASE_URL=http://127.0.0.1:8001
```

Rồi chạy lại React:

```powershell
npm run dev -- --port 5173
```

### Port 5173 bị bận

Chạy:

```powershell
npm run dev -- --port 5174
```

Sau đó mở:

```text
http://127.0.0.1:5174
```

### Full Run báo thiếu OpenAI API key

Kiểm tra file `.env`:

```env
OPENAI_API_KEY=...
```

Sau khi sửa `.env`, tắt backend và chạy lại backend.

### Full Run bị lỗi Java hoặc Maven

Kiểm tra:

- `JAVA_HOME_8`
- `JAVA_HOME_11`
- `JAVA_HOME_17`
- `JAVA_HOME_21`
- `JAVA_HOME_DEFAULT`
- Maven có trong `PATH`
- Các repo đã có trong `repos/` hoặc alias `compiledrepos -> repos` đã được tạo tự động khi runner chạy

Có thể chạy lại bước dựng môi trường:

```powershell
python setup_and_verify.py
```

## 15. Quy trình khuyến nghị

Thứ tự chạy nên dùng:

1. Chạy `python scripts/create_rbl4_samples.py`.
2. Chạy backend.
3. Chạy React.
4. Chạy `Pilot 60` với `Dry Run`.
5. Kiểm tra `Artifacts` có `summary.csv` và `experiment_report.xlsx`.
6. Chạy `Pilot 60` với `Full Run`.
7. Chạy `Report Only` nếu đã có output AgoneTest cũ.
8. Sau khi pilot ổn định mới chạy `Full 300`.

## 16. Lưu ý về bảo mật

Tool không gửi API key về frontend và không xuất API key vào báo cáo.

Tuy nhiên, cần tự đảm bảo:

- Không commit file `.env`.
- Không chụp màn hình hoặc chia sẻ terminal có chứa key.
- Không nộp thư mục `results/runs/` nếu trong log có thông tin nhạy cảm ngoài dự kiến.
