from __future__ import annotations

import json
import shutil
import zipfile
from pathlib import Path
from typing import Any

import pandas as pd


ARTIFACT_NAMES = [
    "metrics_long.csv",
    "summary.csv",
    "generated_failures.csv",
    "baseline_build.csv",
    "generation_classes.csv",
    "generated_tests_manifest.csv",
    "generated_tests.zip",
    "error_summary.csv",
    "skipped_classes.csv",
    "phase_log.csv",
    "api_log.csv",
    "runtime_errors.csv",
    "environment_checks.csv",
    "manifest.json",
    "experiment_report.xlsx",
    "stdout.log",
    "stderr.log",
]


def read_csv_records(path: Path, limit: int | None = None) -> list[dict[str, Any]]:
    if not path.exists() or path.stat().st_size == 0:
        return []
    try:
        df = pd.read_csv(path)
    except Exception:
        return []
    if limit is not None:
        df = df.tail(limit)
    return df.fillna("").to_dict(orient="records")


def write_excel_report(run_dir: Path) -> Path:
    xlsx_path = run_dir / "experiment_report.xlsx"
    sheets = {
        "Summary": run_dir / "summary.csv",
        "Metrics": run_dir / "metrics_long.csv",
        "Failures": run_dir / "generated_failures.csv",
        "Baseline Build": run_dir / "baseline_build.csv",
        "Generation Classes": run_dir / "generation_classes.csv",
        "Generated Tests": run_dir / "generated_tests_manifest.csv",
        "Error Summary": run_dir / "error_summary.csv",
        "Skipped": run_dir / "skipped_classes.csv",
        "Phase Log": run_dir / "phase_log.csv",
        "API Log": run_dir / "api_log.csv",
        "Runtime Errors": run_dir / "runtime_errors.csv",
        "Environment": run_dir / "environment_checks.csv",
    }
    with pd.ExcelWriter(xlsx_path, engine="openpyxl") as writer:
        wrote_sheet = False
        for sheet_name, csv_path in sheets.items():
            if csv_path.exists() and csv_path.stat().st_size > 0:
                try:
                    df = pd.read_csv(csv_path)
                except Exception as exc:
                    df = pd.DataFrame([{"error": f"{type(exc).__name__}: {exc}"}])
            else:
                df = pd.DataFrame()
            df.to_excel(writer, sheet_name=sheet_name[:31], index=False)
            wrote_sheet = True

        manifest_path = run_dir / "manifest.json"
        if manifest_path.exists():
            try:
                manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
                manifest_df = pd.DataFrame(
                    [{"key": key, "value": json.dumps(value, ensure_ascii=False) if isinstance(value, (dict, list)) else value}
                     for key, value in manifest.items()]
                )
            except Exception as exc:
                manifest_df = pd.DataFrame([{"key": "error", "value": f"{type(exc).__name__}: {exc}"}])
            manifest_df.to_excel(writer, sheet_name="Manifest", index=False)
            wrote_sheet = True

        if not wrote_sheet:
            pd.DataFrame([{"message": "No report data available"}]).to_excel(writer, sheet_name="Report", index=False)
    return xlsx_path


def classify_issue(phase: str, status: str, detail: str, error_type: str = "") -> tuple[str, str, str, str]:
    text = f"{phase}\n{status}\n{detail}\n{error_type}".lower()
    if phase.lower() == "baseline_build" and status.upper() in {"FAIL", "ERROR"}:
        return (
            "repo_baseline_build_failed",
            "error",
            "Repo/module không build sạch trước khi sinh test, nên class này phải được loại khỏi bước GPT/EvoSuite và không được tính là lỗi generated test.",
            "Sửa dependency/build tool/module metadata hoặc loại instance khỏi sample; sau đó chạy lại để chỉ sinh test trên repo baseline-pass.",
        )
    if status.upper() == "CANCELLED":
        return (
            "run_cancelled",
            "warning",
            "Run đã bị dừng trước khi pipeline hoàn tất, nên một số report/metric có thể chỉ là dữ liệu tạm.",
            "Chạy lại full-run tới khi completed nếu cần metrics JaCoCo/PIT đầy đủ; partial artifacts chỉ dùng để debug.",
        )
    if (
        "evosuite-maven-plugin" in text
        and "1.2.0" in text
        and (
            "could not be resolved" in text
            or "was not found" in text
            or "failed to transfer" in text
            or "blocked mirror" in text
        )
    ):
        return (
            "evosuite_maven_plugin_unresolved",
            "error",
            "Maven không resolve được org.evosuite.plugins:evosuite-maven-plugin:1.2.0; Maven arm đang gọi plugin remote trong khi artifact này không tải được từ repository hiện tại hoặc đã bị cache fail trong ~/.m2.",
            "Ưu tiên chuyển Maven EvoSuite sang CLI jar local evosuite-1.2.0.jar hoặc cấu hình đúng repository/plugin rồi force update bằng -U và xóa cache fail trong ~/.m2.",
        )
    if "branchcoveragetestfitness" in text and "criterion is not enabled" in text:
        return (
            "evosuite_archive_criterion_mismatch",
            "error",
            "EvoSuite 1.2.0 bị lỗi nội bộ khi archive/criterion không khớp, khiến generate kết thúc mà không ghi test class.",
            "Runner đã cấu hình EvoSuite CLI với -Dtest_archive=false và -Dcriterion=BRANCH; chạy lại bằng phiên bản tool mới để tránh lỗi này.",
        )
    if "attachnotsupportedexception" in text or "no providers installed" in text or "error during attachment" in text:
        return (
            "evosuite_runtime_agent_attach_failure",
            "error",
            "Generated EvoSuite test đang kích hoạt runtime agent/InitializingListener nhưng JVM test fork không attach được agent.",
            "Runner đã sinh EvoSuite test không scaffolding/EvoRunner và bỏ InitializingListener; chạy lại để tránh self-attach khi đo JaCoCo/PIT.",
        )
    if "unsupportedclassversionerror" in text or "unsupported major.minor version" in text:
        return (
            "java_runtime_too_old_for_maven",
            "error",
            "Một Java runtime quá cũ đang chạy Maven/plugin nên không load được class file mới hơn, ví dụ class version 52.0 tương ứng Java 8.",
            "Kiểm tra JAVA_HOME/PATH trong subprocess theo từng repo; dùng JDK đủ mới cho Maven launcher, và chỉ hạ source/target ở compiler config nếu project yêu cầu.",
        )
    if ("file not found" in text or "filenotfounderror" in text) and "testsmelldetection_" in text:
        return (
            "test_smell_output_path_invalid",
            "error",
            "TestSmellDetector tạo file output nhưng bước rename dùng tên module chứa dấu / nên Windows hiểu thành thư mục con và báo không tìm thấy đường dẫn.",
            "Đã vá tool để sanitize module/test_type/prompt trước khi đặt tên file TestSmellDetection.",
        )
    if (
        "could not resolve dependencies" in text
        or "could not find artifact" in text
        or "dependencyresolutionexception" in text
    ):
        return (
            "project_dependency_resolution_error",
            "error",
            "Maven/Gradle không resolve được dependency của project, thường gặp ở multi-module SNAPSHOT khi module phụ chưa được install hoặc thiếu repository nội bộ.",
            "Build/install project từ root trước khi đo, ví dụ mvn install -DskipTests ở root, hoặc loại instance này khỏi sample nếu dependency không thể khôi phục.",
        )
    if "non-parseable pom" in text or "expected root element 'project' but found 'ns0:project'" in text:
        return (
            "pom_rewrite_namespace_error",
            "error",
            "POM bị ghi lại với namespace/prefix không hợp lệ sau khi tool chèn plugin/dependency, khiến Maven không đọc được pom.xml.",
            "Khôi phục pom.xml từ backup/source trước khi chạy lại; cần sửa bước rewrite POM để giữ đúng Maven namespace mặc định.",
        )
    if "dependency management issues found" in text or "version mismatch for plugin" in text:
        return (
            "maven_plugin_management_policy_failure",
            "error",
            "Build fail vì project có rule kiểm tra dependency/plugin management và phát hiện version plugin bị thay đổi hoặc không khớp policy.",
            "Với project có enforcer/dependency-management check, cần đo bằng profile phù hợp hoặc loại khỏi sample nếu việc chèn JaCoCo/PIT/Surefire làm phá policy.",
        )
    if "source 1.6" in text or "source 1.5" in text or "diamond operator is not supported" in text:
        return (
            "java_source_level_incompatible",
            "error",
            "Test/source đang dùng cú pháp Java mới hơn mức compiler của project, ví dụ diamond operator nhưng Maven compile với -source 1.6.",
            "Dùng đúng JDK/source level theo project hoặc sửa/loại test sinh ra dùng cú pháp không tương thích; với thực nghiệm fairness thì nên ghi nhận fail_stage thay vì sửa test.",
        )
    if "build failed" in text and "gradle" in text:
        return (
            "gradle_build_failed",
            "error",
            "Gradle build fail khi chạy test/JaCoCo/PIT; log hiện tại chỉ có stderr rút gọn nên chưa đủ nguyên nhân sâu.",
            "Chạy lại Gradle với --stacktrace/--info hoặc mở log thô của module để biết lỗi dependency, compile, plugin hay test runtime.",
        )
    if "package org.junit.jupiter.api does not exist" in text or "cannot find symbol" in text and "junit" in text:
        return (
            "test_framework_dependency_missing",
            "error",
            "Generated test dùng framework/import không có trong dependency test của module, ví dụ JUnit Jupiter nhưng project chưa có junit-jupiter-api.",
            "Không sửa test sau sinh trong protocol fairness; ghi nhận fail. Nếu chạy pilot debug, cần chọn đúng JUnit version theo project hoặc bổ sung dependency test.",
        )
    if "tests run:" in text and ("<<< failure" in text or "failures:" in text):
        return (
            "generated_test_assertion_failure",
            "error",
            "Test compile và chạy được nhưng assertion fail, nên JaCoCo/PIT không được tính là pass đầy đủ.",
            "Ghi nhận fail_stage; không sửa assertion nếu đang chạy protocol no-repair.",
        )
    if "cannot find symbol" in text or "compilation errors were encountered" in text:
        return (
            "project_compilation_error",
            "error",
            "Project hoặc module Java không compile được khi đo JaCoCo/PIT; đây là lỗi source/dependency/build của repo, không nhất thiết là lỗi GPT.",
            "Xem phase_log.csv để biết file Java và symbol lỗi; cần sửa dependency/source hoặc loại instance khỏi sample nếu precheck không tái lập buildability.",
        )
    if "no compilation errors or general issues found" in text and status.upper() == "FAIL":
        return (
            "metric_failure_without_parser_detail",
            "warning",
            "Maven/Gradle command fail nhưng parser không tìm thấy lỗi compile cụ thể; thường là lỗi plugin, test runtime, PIT/JaCoCo output thiếu, hoặc process return code khác 0.",
            "Mở stdout/stderr, phase_log.csv và output/<project>/*.failed để xem log thô.",
        )
    if ("openai" in text or "api" in text) and status.upper() in {"FAIL", "ERROR"}:
        return (
            "llm_api_error",
            "error",
            "Có lỗi khi gọi model/API hoặc ghi nhận response.",
            "Kiểm tra OPENAI_API_KEY, quota, mạng và api_log.csv.",
        )
    return (
        "unclassified_runtime_issue",
        "warning" if status.upper() == "FAIL" else "error",
        "Tool ghi nhận lỗi nhưng chưa phân loại được tự động.",
        "Xem detail trong phase_log.csv/runtime_errors.csv để phân tích thủ công.",
    )


def build_error_summary(run_dir: Path) -> Path:
    rows: list[dict[str, Any]] = []

    phase_path = run_dir / "phase_log.csv"
    if phase_path.exists() and phase_path.stat().st_size > 0:
        try:
            phase_df = pd.read_csv(phase_path).fillna("")
            failed = phase_df[phase_df["status"].astype(str).str.upper().isin(["FAIL", "ERROR", "CANCELLED"])]
            for _, row in failed.iterrows():
                category, severity, explanation, suggested_action = classify_issue(
                    str(row.get("phase", "")),
                    str(row.get("status", "")),
                    str(row.get("detail", "")),
                )
                rows.append(
                    {
                        "timestamp_utc": row.get("timestamp_utc", ""),
                        "source": "phase_log",
                        "project": row.get("project", ""),
                        "module": row.get("module", ""),
                        "arm": row.get("arm", ""),
                        "focal_class": row.get("focal_class", ""),
                        "phase": row.get("phase", ""),
                        "status": row.get("status", ""),
                        "category": category,
                        "severity": severity,
                        "explanation_vi": explanation,
                        "suggested_action_vi": suggested_action,
                        "detail": str(row.get("detail", ""))[:2000],
                    }
                )
        except Exception as exc:
            rows.append(
                {
                    "timestamp_utc": "",
                    "source": "phase_log",
                    "project": "",
                    "module": "",
                    "arm": "",
                    "focal_class": "",
                    "phase": "error_summary",
                    "status": "ERROR",
                    "category": "error_summary_parse_failed",
                    "severity": "error",
                    "explanation_vi": f"Không đọc được phase_log.csv: {type(exc).__name__}: {exc}",
                    "suggested_action_vi": "Mở phase_log.csv thủ công để kiểm tra định dạng.",
                    "detail": "",
                }
            )

    runtime_path = run_dir / "runtime_errors.csv"
    if runtime_path.exists() and runtime_path.stat().st_size > 0:
        try:
            runtime_df = pd.read_csv(runtime_path).fillna("")
            failed_runtime = runtime_df[runtime_df["status"].astype(str).str.upper().isin(["ERROR", "FAIL"])]
            for _, row in failed_runtime.iterrows():
                detail = f"{row.get('error_type', '')}: {row.get('error_message', '')}\n{row.get('traceback', '')}"
                category, severity, explanation, suggested_action = classify_issue(
                    "runtime_error",
                    str(row.get("status", "")),
                    detail,
                    str(row.get("error_type", "")),
                )
                rows.append(
                    {
                        "timestamp_utc": row.get("timestamp_utc", ""),
                        "source": "runtime_errors",
                        "project": row.get("project", ""),
                        "module": "",
                        "arm": "",
                        "focal_class": "",
                        "phase": "runtime_error",
                        "status": row.get("status", ""),
                        "category": category,
                        "severity": severity,
                        "explanation_vi": explanation,
                        "suggested_action_vi": suggested_action,
                        "detail": detail[:2000],
                    }
                )
        except Exception:
            pass

    out_path = run_dir / "error_summary.csv"
    columns = [
        "timestamp_utc",
        "source",
        "project",
        "module",
        "arm",
        "focal_class",
        "phase",
        "status",
        "category",
        "severity",
        "explanation_vi",
        "suggested_action_vi",
        "detail",
    ]
    pd.DataFrame(rows, columns=columns).to_csv(out_path, index=False)
    return out_path


def collect_generated_tests(
    run_dir: Path,
    output_dir: Path,
    projects: list[str],
    modified_after_epoch: float | None = None,
) -> Path:
    generated_dir = run_dir / "generated_tests"
    if generated_dir.exists():
        shutil.rmtree(generated_dir)
    generated_dir.mkdir(parents=True, exist_ok=True)

    rows: list[dict[str, Any]] = []
    for project in sorted(set(str(project) for project in projects)):
        project_output = output_dir / project
        if not project_output.exists():
            continue
        for source_path in sorted(project_output.glob("response_*.java")):
            if modified_after_epoch is not None and source_path.stat().st_mtime < modified_after_epoch:
                continue
            file_name = source_path.name
            arm = "evosuite" if file_name.startswith("response_evosuite") else "gpt" if "gpt" in file_name else "unknown"
            target_dir = generated_dir / project
            target_dir.mkdir(parents=True, exist_ok=True)
            target_path = target_dir / file_name
            shutil.copy2(source_path, target_path)
            rows.append(
                {
                    "project": project,
                    "arm": arm,
                    "file_name": file_name,
                    "source_path": str(source_path),
                    "stored_path": str(target_path),
                    "size_bytes": target_path.stat().st_size,
                    "modified_at": pd.Timestamp.fromtimestamp(target_path.stat().st_mtime).isoformat(),
                }
            )

    manifest_path = run_dir / "generated_tests_manifest.csv"
    pd.DataFrame(
        rows,
        columns=["project", "arm", "file_name", "source_path", "stored_path", "size_bytes", "modified_at"],
    ).to_csv(manifest_path, index=False)

    zip_path = run_dir / "generated_tests.zip"
    if zip_path.exists():
        zip_path.unlink()
    with zipfile.ZipFile(zip_path, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for file_path in generated_dir.rglob("*.java"):
            archive.write(file_path, file_path.relative_to(generated_dir))
    return zip_path


def artifact_infos(run_dir: Path) -> list[dict[str, Any]]:
    artifacts: list[dict[str, Any]] = []
    for name in ARTIFACT_NAMES:
        path = run_dir / name
        if not path.exists():
            continue
        stat = path.stat()
        artifacts.append(
            {
                "name": name,
                "path": str(path),
                "size_bytes": stat.st_size,
                "modified_at": pd.Timestamp.fromtimestamp(stat.st_mtime).isoformat(),
            }
        )
    return artifacts
