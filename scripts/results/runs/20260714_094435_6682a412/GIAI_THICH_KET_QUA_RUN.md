# Giải thích kết quả run `20260714_094435_6682a412`

Run folder:

```text
D:\pilot_delivery\pilot_delivery\pilot_bundle_24\results\runs\20260714_094435_6682a412
```

Run này đã chạy xong. Trạng thái đúng nằm trong `manifest.json`:

```text
status = completed
started = 2026-07-14T02:44:35+00:00
completed = 2026-07-14T03:31:41+00:00
duration ≈ 47 phút 06 giây
```

Lưu ý: `status.json` vẫn ghi `"status": "running"` và `return_code: null`. Đây là trạng thái cũ lúc backend tạo process, chưa được ghi đè lại sau khi runner kết thúc. Khi đọc kết quả cuối cùng, ưu tiên `manifest.json`.

## 1. Tổng quan kết quả

Input:

- `source_sample_n = 24`
- `buildable_run_n = 23`
- `precheck_skipped_n = 1`
- Model GPT: `gpt-4o-mini-2024-07-18`
- Prompt: `rbl4-zero-shot`
- Arms: `evosuite`, `gpt-4o-mini-2024-07-18`

Dòng bị skip trước khi chạy:

| Project | Class | Lý do |
|---|---|---|
| `88231534` | `TracingCommandListener` | `test_file_missing`: thiếu file test gốc ở `compiledrepos/88231534/opentracing-mongo-common/src/test/java/io/opentracing/contrib/mongo/common/TracingCommandListenerTest.java` |

Kết quả đo cuối cùng từ `summary.csv`:

| Arm | Compile/measure thành công | Tỷ lệ trên 23 buildable | Ý nghĩa |
|---|---:|---:|---|
| EvoSuite | 6 | 26.09% | Có 6 test class EvoSuite build được và có metric JaCoCo/PIT |
| GPT | 1 | 4.35% | Chỉ 1 test class GPT build được và có metric JaCoCo/PIT |

Nếu nhìn phase `maven_verify_jacoco_pit PASS` thì sẽ thấy 7 lần đo thành công. Con số 7 này là theo arm/test artifact, không phải 7 class nguồn khác nhau:

- EvoSuite pass: 6 artifact.
- GPT pass: 1 artifact.
- `SimpleClassScanner` pass ở cả EvoSuite và GPT, nên số class nguồn duy nhất có ít nhất một arm pass là 6.

## 2. Toàn bộ file/thư mục trong run folder

| File/thư mục | Vai trò |
|---|---|
| `api_log.csv` | Log từng lần gọi GPT: model, thời gian, token, status. Đây là nguồn chính để biết GPT API có lỗi hay không. |
| `backups/` | Bản backup các file staging/output cũ trước khi runner ghi file mới. Dùng để audit và phục hồi nếu cần. |
| `environment_checks.csv` | Kết quả kiểm tra môi trường: OpenAI key, OpenAI SDK/httpx, JDK 7/8/11/17/21/24/25/26, EvoSuite jar, TestSmellDetector jar. |
| `error_summary.csv` | Bản tổng hợp lỗi đã phân loại tự động từ `phase_log.csv` và log build. Dùng để xem nhanh nhóm lỗi. |
| `experiment_report.xlsx` | Báo cáo Excel tổng hợp nhiều sheet: Summary, Metrics, Failures, Skipped, Phase Log, API Log, Manifest. |
| `generated_failures.csv` | Các row không compile/không có metric, kèm `fail_stage`, marker lỗi, file generated nếu có. |
| `generated_tests/` | Bản copy các test class sinh ra bởi GPT/EvoSuite trong run này, gom theo `project`. |
| `generated_tests_manifest.csv` | Manifest của các file test sinh ra: project, arm, file name, source path, stored path, size, modified time. |
| `generated_tests.zip` | File zip chứa toàn bộ `generated_tests/` để nén/chuyển máy dễ hơn. |
| `manifest.json` | Metadata chính xác của run sau khi kết thúc: status completed, sample, model, prompt, project run/skipped, policy. |
| `metrics_long.csv` | Bảng metric dài theo từng `(class, arm)`: generated file, compile, coverage, mutation, fail stage. Đây là file quan trọng nhất để phân tích từng class. |
| `phase_log.csv` | Timeline realtime của pipeline: precheck, stage, toolchain, evosuite_generate, maven/gradle Jacoco/PIT, report. |
| `project_info.json` | Project/module metadata được stage cho AgoneTest: build tool, Java version, JUnit/TestNG, modules. |
| `runtime_errors.csv` | Lỗi runtime của runner theo project. Trong run này các project đều ghi `OK`, tức runner không crash. |
| `skipped_classes.csv` | Các row bị loại ở precheck trước khi chạy. Run này có 1 row: `88231534`. |
| `staged_classes.csv` | 23 row đã qua precheck và được stage vào AgoneTest. |
| `status.json` | Trạng thái ban đầu/đang chạy của process backend. File này đang stale, không phản ánh trạng thái cuối. |
| `stderr.log` | Log stderr của runner/process con, chủ yếu log version Java và stderr build. |
| `stdout.log` | Log stdout đầy đủ của AgoneTest: project processing, file generated, Maven/Gradle errors. |
| `summary.csv` | Tổng hợp thống kê cuối cùng theo arm: compile success, coverage mean/median, mutation mean/median, p-value mô tả. |

Các file trong `backups/`:

| File backup | Ý nghĩa |
|---|---|
| `AgoneTest__run_settings.yaml` | Backup setting prompt/model cũ. |
| `output__classes.csv` | Backup class staging cũ. |
| `output__project_info.json` | Backup project info cũ. |
| `output__output_agone_classes.csv` | Backup output AgoneTest classes cũ. |
| `output__output_agone_classes_filtered.csv` | Backup output classes filtered cũ. |
| `output__output_agone_info.txt` | Backup info output cũ. |
| `output__output_agone_mean.csv` | Backup mean metric cũ. |
| `output__output_agone_mean_filtered.csv` | Backup mean filtered cũ. |
| `output__output_agone_projects.csv` | Backup project output cũ. |

## 3. Test class được sinh ra bao nhiêu file?

Từ `generated_tests_manifest.csv`:

| Arm | Số file generated được lưu |
|---|---:|
| GPT | 21 |
| EvoSuite | 9 |

Vì vậy run này không thật sự có đủ 24 file GPT trong artifact cuối:

- 1 row bị skip trước khi chạy: `88231534`.
- 2 row buildable nhưng không có output GPT/EvoSuite: `58086354`, `58767125`.
- 21 row còn lại có file GPT.
- 9 row có file EvoSuite.

Hai row `58086354` và `58767125` không sinh output vì tool stage sai build tool/module:

| Project | Class | Lý do |
|---|---|---|
| `58086354` | `OkHttpClientStream` | CSV/project_info coi module là Maven và tìm `compiledrepos/58086354/demo/gRPC/okhttp/pom.xml`, nhưng module thật có `build.gradle`, không có `pom.xml`. Log: `No such file or directory ... demo/gRPC/okhttp\pom.xml`. |
| `58767125` | `BigComplex` | CSV/project_info coi module là Maven và tìm `compiledrepos/58767125/ch.obermuhlner.math.big/pom.xml`, nhưng module thật có `build.gradle`, còn `pom.xml` nằm ở root project. Log: `No such file or directory ... ch.obermuhlner.math.big\pom.xml`. |

Nói ngắn gọn: hai project này bị metadata `Build_Tool=maven` trong CSV pilot làm runner chọn sai nhánh Maven, trong khi module thực tế là Gradle hoặc có POM ở root khác module.

## 4. Vì sao chỉ có 7 lần đo thành công?

Theo `metrics_long.csv`:

| Arm | generated file exists | Compile/metric thành công | Fail |
|---|---:|---:|---:|
| EvoSuite | 9 | 6 | 17 |
| GPT | 21 | 1 | 22 |

EvoSuite có 11 lần `evosuite_generate PASS` trong `phase_log.csv`, nhưng chỉ 6 artifact EvoSuite compile/measure thành công. Lý do: `evosuite_generate PASS` chỉ có nghĩa EvoSuite sinh được test class. Sau đó test class vẫn phải qua Maven/Gradle + JaCoCo + PIT. Một số test EvoSuite sinh được nhưng fail ở build/metric.

Các artifact đo thành công:

| Arm | Project | Class | Branch | Line | Method | Mutation |
|---|---|---|---:|---:|---:|---:|
| EvoSuite | `6489406` | `ScenarioTracker` | 9.09 | 32.08 | 66.67 | 10.67 |
| EvoSuite | `37489990` | `TraceValve` | 28.57 | 21.05 | 60.00 | 0.00 |
| EvoSuite | `50552134` | `SimpleClassScanner` | 33.33 | 40.00 | 71.43 | 11.11 |
| EvoSuite | `771158` | `StatementComparator` | 50.00 | 61.11 | 100.00 | 14.29 |
| EvoSuite | `89562636` | `CoapServer` | 52.94 | 61.47 | 71.88 | 13.04 |
| EvoSuite | `54620819` | `GolangBuildMojo` | 77.50 | 77.63 | 90.48 | 17.50 |
| GPT | `50552134` | `SimpleClassScanner` | 91.67 | 90.00 | 100.00 | 77.78 |

## 5. Kết quả từng class

`gen=1` nghĩa là có file test sinh ra. `compile=1` nghĩa là build/JaCoCo/PIT đo được.

| Row | Project | Class | GPT gen | GPT compile | GPT fail_stage | Evo gen | Evo compile | Evo fail_stage |
|---:|---|---|---:|---:|---|---:|---:|---|
| 0 | `58086354` | `OkHttpClientStream` | 0 | 0 | `missing_output` | 0 | 0 | `missing_output` |
| 1 | `6489406` | `ScenarioTracker` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 1 | `ok` |
| 2 | `49126308` | `ElasticScroll` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 3 | `39770930` | `WaitAvailableBrowsersChecker` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 4 | `37489990` | `TraceValve` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 1 | `ok` |
| 5 | `5093728` | `FeatureContext` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 0 | `generated_test_no_successful_build_or_metrics` |
| 6 | `7283919` | `TeradataDatePeriod` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 7 | `58767125` | `BigComplex` | 0 | 0 | `missing_output` | 0 | 0 | `missing_output` |
| 8 | `56342003` | `AbstractService` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 9 | `74217` | `FieldLocator` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 10 | `74458764` | `CookieSessionTracking` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 11 | `54231507` | `EventFiringWebDriver` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 0 | `generated_test_no_successful_build_or_metrics` |
| 12 | `37728390` | `AttributeValue` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 14 | `50552134` | `SimpleClassScanner` | 1 | 1 | `ok` | 1 | 1 | `ok` |
| 15 | `771158` | `StatementComparator` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 1 | `ok` |
| 16 | `5175291` | `Strings` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 0 | `generated_test_no_successful_build_or_metrics` |
| 17 | `3526892` | `Pricing` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 18 | `812511` | `SimpleMemoryCertStore` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 19 | `57251243` | `SpanCustomizingApplicationEventListener` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 20 | `70711909` | `GetHereClientCredentialsIdTokenTutorial` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 21 | `89562636` | `CoapServer` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 1 | `ok` |
| 22 | `38080174` | `OTAlgorithms` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 0 | 0 | `generation_or_build_failed_marker` |
| 23 | `54620819` | `GolangBuildMojo` | 1 | 0 | `generated_test_no_successful_build_or_metrics` | 1 | 1 | `ok` |

## 6. GPT có lỗi API không?

Không. Theo `api_log.csv`:

- Tổng GPT API calls: 21.
- Status: 21/21 `OK`.
- Không có `error_type`.
- Không có `error_message`.

Vì vậy không phải GPT API không chạy. GPT đã sinh code cho 21 class. Vấn đề là code test sau khi được sinh không compile/build/measure được trong project thật.

GPT chỉ compile/measure thành công 1 artifact:

| Project | Class | File |
|---|---|---|
| `50552134` | `SimpleClassScanner` | `output/50552134/response_gpt-4o-mini-2024-07-18_rbl4-zero-shot_SimpleClassScannerTest.java` |

Các GPT artifact còn lại fail chủ yếu vì 2 nhóm:

1. Project/build environment không resolve được dependency, nên dù GPT code có thể đúng vẫn không build được.
2. GPT sinh code không khớp API thật của project: gọi sai constructor, thiếu import/dependency test, dùng lambda khi source level cũ, truy cập private field/method, dùng Mockito/JUnit khi project không có dependency tương ứng.

Ví dụ lỗi GPT cụ thể từ `stdout.log`:

| Project | Class | Nhóm lỗi GPT |
|---|---|---|
| `3526892` | `Pricing` | Maven dependency không resolve: `spy:spymemcached:jar:2.8.1`. |
| `37489990` | `TraceValve` | Compile lỗi `cannot find symbol LifecycleException`. |
| `37728390` | `AttributeValue` | Parent POM/dependency không resolve từ `maven.geotoolkit.org`. |
| `38080174` | `OTAlgorithms` | Thiếu dependency nội bộ `datakernel-*.jar:3.2-SNAPSHOT`. |
| `39770930` | `WaitAvailableBrowsersChecker` | Thiếu dependency `gridrouter-config:1.32-SNAPSHOT`. |
| `49126308` | `ElasticScroll` | Maven dependency resolution failed. |
| `5093728` | `FeatureContext` | Compile lỗi `cannot find symbol`. |
| `5175291` | `Strings` | Thiếu `org.junit`, và GPT dùng lambda trong project đang build với `-source 1.7`. |
| `54231507` | `EventFiringWebDriver` | Thiếu JUnit symbol, truy cập field private `listeners`, gọi method không tồn tại. |
| `54620819` | `GolangBuildMojo` | Thiếu Mockito, truy cập nhiều field/method private của `GolangBuildMojo`. |
| `56342003` | `AbstractService` | Thiếu dependency `kangaroo-common:1.1.0-SNAPSHOT`. |
| `57251243` | `SpanCustomizingApplicationEventListener` | Gradle `BUILD FAILED`. |
| `6489406` | `ScenarioTracker` | GPT gọi sai constructor `AisMessage5`; lỗi `long cannot be converted to Vdm`. |
| `70711909` | `GetHereClientCredentialsIdTokenTutorial` | Thiếu dependency Maven của HERE OAuth example. |
| `7283919` | `TeradataDatePeriod` | Gradle `BUILD FAILED`. |
| `74217` | `FieldLocator` | Thiếu dependency `flapjack-annotation:1.0.4-SNAPSHOT`. |
| `74458764` | `CookieSessionTracking` | `cannot find symbol`, truy cập private field/method `secure`, `contextPath`, `cookiePath()`. |
| `771158` | `StatementComparator` | Thiếu Mockito, nhiều `cannot find symbol`. |
| `812511` | `SimpleMemoryCertStore` | Maven dependency không resolve cho `ssl-proxies`. |
| `89562636` | `CoapServer` | GPT dùng callback/lambda không khớp interface; truy cập private field `enabledCriticalOptTest`. |

## 7. Vì sao các class khác lỗi?

Các lỗi không cùng một nguyên nhân. Từ `error_summary.csv`, nhóm lỗi chính là:

| Nhóm lỗi | Số lần | Ý nghĩa |
|---|---:|---|
| `project_dependency_resolution_error` | 8 | Maven/Gradle không tải được dependency, thiếu module SNAPSHOT, thiếu repository cũ/nội bộ. |
| `project_compilation_error` | 8 | Test class sinh ra không compile: sai API, sai import, private access, lambda/source mismatch. |
| `test_framework_dependency_missing` | 3 | Thiếu JUnit/Mockito/hamcrest hoặc dependency test tương ứng. |
| `gradle_build_failed` | 2 | Gradle task fail, log chi tiết nằm trong `stdout.log`/`phase_log.csv`. |
| `unclassified_runtime_issue` | 11 | Lỗi tool/build chưa phân loại tự động, thường rơi vào EvoSuite generation fail hoặc log Maven bị cắt. |

Một số nguyên nhân quan trọng:

- Repo/module không tự build độc lập: nhiều project là multi-module SNAPSHOT, module con cần install root project trước.
- Repository cũ chết hoặc không truy cập được: ví dụ `maven.geotoolkit.org`, snapshot repositories, dependency nội bộ.
- CSV metadata sai build tool: `58086354`, `58767125` bị chọn Maven trong khi module thực tế có `build.gradle`.
- GPT zero-shot không sửa code theo feedback. Theo fairness policy, tool không repair generated test, không thêm dependency, không sửa source, nên test sai là ghi fail.
- EvoSuite sinh được file chưa chắc build được. Sau generation vẫn phải qua Maven/Gradle + JaCoCo + PIT.

## 8. Vì sao run lâu?

`phase_log.csv` cho thấy project chậm nhất là:

| Project | Thời gian |
|---|---:|
| `5093728` | `1542.021s` ≈ 25.7 phút |

Các project EvoSuite pass thường mất khoảng 70-80 giây vì EvoSuite đang chạy với `search_budget=60`. Riêng `5093728` có gap lớn trước `toolchain`, khả năng nằm ở scan/setup/build classpath của AgoneTest hoặc Maven project preparation.

Top project lâu nhất:

| Project | Duration sec |
|---|---:|
| `5093728` | 1542.021 |
| `37489990` | 136.511 |
| `6489406` | 133.675 |
| `89562636` | 133.370 |
| `50552134` | 121.920 |
| `54620819` | 120.006 |
| `54231507` | 108.212 |
| `771158` | 104.755 |
| `5175291` | 104.085 |

## 9. Kết luận

Run đã chạy hết pipeline và xuất báo cáo đầy đủ. Kết quả thấp không phải vì GPT API không được gọi, mà vì phần lớn generated test không qua được bước build/measurement.

Kết quả cuối nên hiểu như sau:

- 24 class trong sample.
- 23 class đi vào runner.
- 1 class bị skip do thiếu test file gốc.
- GPT API gọi thành công 21 lần và sinh 21 file.
- GPT chỉ có 1 file build/measure thành công.
- EvoSuite sinh được 9 file được lưu, trong đó 6 file build/measure thành công.
- Tổng số artifact đo thành công là 7, nhưng số class nguồn duy nhất có kết quả thành công là 6.

Các file cần xem khi debug tiếp:

1. `summary.csv`: số liệu tổng hợp.
2. `metrics_long.csv`: kết quả từng class/arm.
3. `generated_failures.csv`: các class fail theo arm.
4. `error_summary.csv`: nhóm lỗi đã phân loại.
5. `phase_log.csv`: timeline realtime và detail lỗi theo phase.
6. `stdout.log`: log chi tiết nhất, chứa lỗi Maven/Gradle/compile đầy đủ hơn.
7. `generated_tests_manifest.csv`: danh sách file test thật sự được sinh và lưu.
