from __future__ import annotations

import json
import importlib
import os
import subprocess
import sys
import uuid
from pathlib import Path
from typing import Any

import pandas as pd
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse, StreamingResponse

from .events import append_event, event_stream, read_events
from .reports import (
    ARTIFACT_NAMES,
    artifact_infos,
    build_error_summary,
    collect_generated_tests,
    read_csv_records,
    write_excel_report,
)
from .schemas import (
    ApiCallInfo,
    ArtifactInfo,
    DEFAULT_MODEL,
    DEFAULT_PROMPT,
    GeneratedTestInfo,
    RunCreateRequest,
    RunInfo,
    SummaryPayload,
    utc_now,
)


BASE_DIR = Path(__file__).resolve().parents[1]
RUNS_DIR = BASE_DIR / "results" / "runs"
RUNS_DIR.mkdir(parents=True, exist_ok=True)
SAMPLE_MAP = {
    "full_300": BASE_DIR / "output" / "classes_main.csv",
    "pilot_60": BASE_DIR / "output" / "pilot_60_classes.csv",
    "part1": BASE_DIR / "output" / "classes_part1.csv",
    "part2": BASE_DIR / "output" / "classes_part2.csv",
    "part3": BASE_DIR / "output" / "classes_part3.csv",
    "pilot_24": BASE_DIR / "output" / "pilot_24_classes.csv",
}
processes: dict[str, subprocess.Popen[Any]] = {}


app = FastAPI(title="RBL-4 Experiment Tool", version="1.0.0")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://127.0.0.1:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


def resolve_sample(request: RunCreateRequest) -> Path:
    if request.sample_key == "custom":
        if not request.custom_sample_csv:
            raise HTTPException(status_code=400, detail="custom_sample_csv is required for custom sample")
        sample = Path(request.custom_sample_csv)
        if not sample.is_absolute():
            sample = BASE_DIR / sample
    else:
        sample = SAMPLE_MAP[request.sample_key]
    sample = sample.resolve()
    try:
        sample.relative_to(BASE_DIR.resolve())
    except ValueError as exc:
        raise HTTPException(status_code=400, detail="Sample CSV must live inside data2/class2test") from exc
    if not sample.exists():
        raise HTTPException(status_code=404, detail=f"Sample CSV not found: {sample}")
    return sample


def write_status(run_dir: Path, payload: dict[str, Any]) -> None:
    (run_dir / "status.json").write_text(json.dumps(payload, indent=2, ensure_ascii=False), encoding="utf-8")


def read_json(path: Path) -> dict[str, Any]:
    if not path.exists():
        return {}
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except Exception:
        return {}


def run_has_runtime_error(run_dir: Path) -> bool:
    if (run_dir / "error.log").exists():
        return True
    runtime_errors = run_dir / "runtime_errors.csv"
    if not runtime_errors.exists():
        return False
    try:
        return ",ERROR," in runtime_errors.read_text(encoding="utf-8", errors="ignore")
    except Exception:
        return False


def run_info(run_id: str) -> RunInfo:
    run_dir = RUNS_DIR / run_id
    if not run_dir.exists():
        raise HTTPException(status_code=404, detail="Run not found")
    status_doc = read_json(run_dir / "status.json")
    manifest = read_json(run_dir / "manifest.json")
    proc = processes.get(run_id)
    return_code = proc.poll() if proc else status_doc.get("return_code")

    manifest_status = manifest.get("status")
    if manifest_status == "completed":
        status = "completed"
    elif manifest_status == "failed":
        status = "failed"
    elif manifest_status == "cancelled":
        status = "cancelled"
    elif proc and return_code is None:
        status = "running"
    elif status_doc.get("status") == "running" and not proc and run_has_runtime_error(run_dir):
        status = "failed"
    elif return_code not in (None, 0):
        status = "failed"
    else:
        status = status_doc.get("status", "unknown")

    return RunInfo(
        run_id=run_id,
        status=status,
        run_mode=manifest.get("run_mode") or status_doc.get("run_mode", "unknown"),
        sample_csv=manifest.get("source_sample_csv") or status_doc.get("sample_csv", ""),
        model=manifest.get("model") or status_doc.get("model", DEFAULT_MODEL),
        prompt=manifest.get("prompt") or status_doc.get("prompt", DEFAULT_PROMPT),
        created_at=status_doc.get("created_at", ""),
        started_at=status_doc.get("started_at"),
        completed_at=manifest.get("timestamp_utc") if status in {"completed", "failed", "cancelled"} else None,
        pid=status_doc.get("pid"),
        return_code=return_code,
        source_sample_n=manifest.get("source_sample_n"),
        buildable_run_n=manifest.get("buildable_run_n"),
        precheck_skipped_n=manifest.get("precheck_skipped_n"),
        baseline_pass_n=manifest.get("baseline_pass_n"),
        baseline_failed_n=manifest.get("baseline_failed_n"),
        generation_run_n=manifest.get("generation_run_n"),
        error=manifest.get("error") or status_doc.get("error"),
        artifacts=[ArtifactInfo(**item) for item in artifact_infos(run_dir)],
    )


def generated_test_infos(run_dir: Path) -> list[GeneratedTestInfo]:
    items: list[GeneratedTestInfo] = []
    for record in read_csv_records(run_dir / "generated_tests_manifest.csv", limit=100):
        try:
            size_bytes = int(float(record.get("size_bytes") or 0))
        except (TypeError, ValueError):
            size_bytes = 0
        items.append(
            GeneratedTestInfo(
                project=str(record.get("project", "")),
                arm=str(record.get("arm", "")),
                file_name=str(record.get("file_name", "")),
                source_path=str(record.get("source_path", "")),
                stored_path=str(record.get("stored_path", "")),
                size_bytes=size_bytes,
                modified_at=str(record.get("modified_at", "")) or None,
            )
        )
    return items


def api_call_infos(run_dir: Path) -> list[ApiCallInfo]:
    items: list[ApiCallInfo] = []
    for record in read_csv_records(run_dir / "api_log.csv", limit=100):
        items.append(
            ApiCallInfo(
                timestamp_utc=str(record.get("timestamp_utc", "")),
                model_requested=str(record.get("model_requested", "")),
                model_returned=str(record.get("model_returned", "")),
                duration_sec=record.get("duration_sec", ""),
                prompt_tokens=record.get("prompt_tokens", ""),
                completion_tokens=record.get("completion_tokens", ""),
                total_tokens=record.get("total_tokens", ""),
                status=str(record.get("status", "")),
                error_type=str(record.get("error_type", "")),
                error_message=str(record.get("error_message", "")),
            )
        )
    return items


def write_partial_cancel_artifacts(run_dir: Path, status_doc: dict[str, Any]) -> None:
    phase_log = run_dir / "phase_log.csv"
    cancel_detail = "Run cancelled; writing partial diagnostics and artifacts"
    if not phase_log.exists() or cancel_detail not in phase_log.read_text(encoding="utf-8", errors="ignore"):
        append_event(phase_log, "run", "CANCELLED", cancel_detail)

    staged_classes = run_dir / "staged_classes.csv"
    projects: list[str] = []
    if staged_classes.exists() and staged_classes.stat().st_size > 0:
        try:
            staged_df = pd.read_csv(staged_classes)
            if "Project" in staged_df.columns:
                projects = sorted(staged_df["Project"].astype(str).unique().tolist())
        except Exception:
            projects = []

    if status_doc.get("run_mode") != "dry_run" and projects:
        try:
            started_at = status_doc.get("started_at")
            modified_after_epoch = pd.Timestamp(started_at).timestamp() if started_at else None
            collect_generated_tests(
                run_dir,
                BASE_DIR / "output",
                projects,
                modified_after_epoch=modified_after_epoch,
            )
        except Exception:
            pass
    elif not (run_dir / "generated_tests_manifest.csv").exists():
        pd.DataFrame(
            columns=["project", "arm", "file_name", "source_path", "stored_path", "size_bytes", "modified_at"]
        ).to_csv(run_dir / "generated_tests_manifest.csv", index=False)

    try:
        write_partial_metrics(run_dir, status_doc)
    except Exception:
        pass

    build_error_summary(run_dir)
    manifest = read_json(run_dir / "manifest.json")
    manifest.update(
        {
            "timestamp_utc": utc_now(),
            "status": "cancelled",
            "error": status_doc.get("error", "Cancelled by user"),
            "run_id": status_doc.get("run_id", run_dir.name),
            "run_mode": status_doc.get("run_mode", "unknown"),
            "source_sample_csv": status_doc.get("sample_csv", ""),
            "model": status_doc.get("model", DEFAULT_MODEL),
            "prompt": status_doc.get("prompt", DEFAULT_PROMPT),
            "projects_run": projects,
        }
    )
    (run_dir / "manifest.json").write_text(json.dumps(manifest, indent=2, ensure_ascii=False), encoding="utf-8")
    write_excel_report(run_dir)


def write_partial_metrics(run_dir: Path, status_doc: dict[str, Any]) -> None:
    staged_classes = run_dir / "staged_classes.csv"
    if not staged_classes.exists() or staged_classes.stat().st_size == 0:
        return
    sample_df = pd.read_csv(staged_classes)
    if sample_df.empty:
        return

    old_cwd = Path.cwd()
    os.chdir(BASE_DIR)
    if str(BASE_DIR) not in sys.path:
        sys.path.insert(0, str(BASE_DIR))
    try:
        legacy = importlib.import_module("run_rbl4_part1_buildable_experiment")
        model = status_doc.get("model", DEFAULT_MODEL)
        prompt = status_doc.get("prompt", DEFAULT_PROMPT)
        run_mode = status_doc.get("run_mode", "full_run")
        sample_csv_text = status_doc.get("sample_csv", "")
        sample_csv = Path(sample_csv_text) if sample_csv_text else None
        if sample_csv and sample_csv.exists():
            source_n = int(len(pd.read_csv(sample_csv)))
        else:
            source_n = int(len(sample_df))
        skipped_path = run_dir / "skipped_classes.csv"
        skipped_n = int(len(pd.read_csv(skipped_path))) if skipped_path.exists() and skipped_path.stat().st_size > 0 else 0
        baseline_path = run_dir / "baseline_build.csv"
        baseline_df = pd.read_csv(baseline_path) if baseline_path.exists() and baseline_path.stat().st_size > 0 else None
        legacy.DEFAULT_SAMPLE = sample_csv if sample_csv and sample_csv.exists() else staged_classes
        metrics_df = legacy.build_metrics_long(sample_df, model, prompt, run_mode, baseline_df=baseline_df if run_mode == "full_run" else None)
        metrics_df["source_sample"] = str(sample_csv) if sample_csv else ""
        summary_df = legacy.build_summary(metrics_df, source_n=source_n, skipped_n=skipped_n)
        if "run_scope" in summary_df.columns:
            summary_df["run_scope"] = "rbl4_experiment_tool_cancelled_partial"
        metrics_df.to_csv(run_dir / "metrics_long.csv", index=False)
        failures_df = metrics_df[(metrics_df["compilation"] != 1) | (metrics_df["fail_stage"] != "ok")].copy()
        failures_df.to_csv(run_dir / "generated_failures.csv", index=False)
        summary_df.to_csv(run_dir / "summary.csv", index=False)
    finally:
        os.chdir(old_cwd)


@app.get("/api/health")
def health() -> dict[str, str]:
    return {"status": "ok", "base_dir": str(BASE_DIR)}


@app.post("/api/runs", response_model=RunInfo)
def create_run(request: RunCreateRequest) -> RunInfo:
    sample = resolve_sample(request)
    run_id = f"{pd.Timestamp.now().strftime('%Y%m%d_%H%M%S')}_{uuid.uuid4().hex[:8]}"
    run_dir = RUNS_DIR / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    stdout = (run_dir / "stdout.log").open("w", encoding="utf-8")
    stderr = (run_dir / "stderr.log").open("w", encoding="utf-8")

    command = [
        sys.executable,
        "-m",
        "experiment_tool.runner",
        "--run-id",
        run_id,
        "--sample-csv",
        str(sample),
        "--mode",
        request.run_mode,
        "--model",
        request.model,
        "--prompt",
        request.prompt,
    ]
    if request.resume:
        command.append("--resume")
    if request.clear_agone_output:
        command.append("--clear-agone-output")

    env = os.environ.copy()
    env["PYTHONPATH"] = str(BASE_DIR)
    process = subprocess.Popen(command, cwd=BASE_DIR, stdout=stdout, stderr=stderr, env=env)
    processes[run_id] = process
    write_status(
        run_dir,
        {
            "run_id": run_id,
            "status": "running",
            "created_at": utc_now(),
            "started_at": utc_now(),
            "sample_csv": str(sample),
            "run_mode": request.run_mode,
            "model": request.model,
            "prompt": request.prompt,
            "pid": process.pid,
            "return_code": None,
        },
    )
    return run_info(run_id)


@app.get("/api/runs", response_model=list[RunInfo])
def list_runs() -> list[RunInfo]:
    runs = []
    for path in sorted(RUNS_DIR.iterdir(), key=lambda item: item.stat().st_mtime, reverse=True):
        if path.is_dir():
            with suppress_http_404():
                runs.append(run_info(path.name))
    return runs


class suppress_http_404:
    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc, tb):
        return isinstance(exc, HTTPException) and exc.status_code == 404


@app.get("/api/runs/{run_id}", response_model=SummaryPayload)
def get_run(run_id: str) -> SummaryPayload:
    run = run_info(run_id)
    run_dir = RUNS_DIR / run_id
    return SummaryPayload(
        run=run,
        summary=read_csv_records(run_dir / "summary.csv"),
        diagnostics=read_csv_records(run_dir / "error_summary.csv", limit=50),
        generated_tests=generated_test_infos(run_dir),
        api_calls=api_call_infos(run_dir),
        metrics_preview=read_csv_records(run_dir / "metrics_long.csv", limit=200),
        recent_events=read_events(run_dir / "phase_log.csv", limit=100),
    )


@app.get("/api/runs/{run_id}/events")
async def stream_run_events(run_id: str) -> StreamingResponse:
    run_dir = RUNS_DIR / run_id
    if not run_dir.exists():
        raise HTTPException(status_code=404, detail="Run not found")

    def done() -> bool:
        status = run_info(run_id).status
        return status in {"completed", "failed", "cancelled"}

    return StreamingResponse(event_stream(run_dir / "phase_log.csv", stop_when_done=done), media_type="text/event-stream")


@app.post("/api/runs/{run_id}/cancel", response_model=RunInfo)
def cancel_run(run_id: str) -> RunInfo:
    proc = processes.get(run_id)
    run_dir = RUNS_DIR / run_id
    if not run_dir.exists():
        raise HTTPException(status_code=404, detail="Run not found")
    if proc and proc.poll() is None:
        proc.terminate()
        try:
            proc.wait(timeout=10)
        except subprocess.TimeoutExpired:
            proc.kill()
    status_doc = read_json(run_dir / "status.json")
    status_doc.update({"status": "cancelled", "return_code": proc.returncode if proc else None, "error": "Cancelled by user"})
    write_status(run_dir, status_doc)
    try:
        write_partial_cancel_artifacts(run_dir, status_doc)
    except Exception as exc:
        status_doc["artifact_error"] = f"{type(exc).__name__}: {exc}"
        write_status(run_dir, status_doc)
    return run_info(run_id)


@app.get("/api/runs/{run_id}/artifacts/{artifact_name}")
def download_artifact(run_id: str, artifact_name: str) -> FileResponse:
    if artifact_name not in ARTIFACT_NAMES and artifact_name != "error.log":
        raise HTTPException(status_code=404, detail="Unknown artifact")
    path = RUNS_DIR / run_id / artifact_name
    if not path.exists():
        raise HTTPException(status_code=404, detail="Artifact not found")
    return FileResponse(path, filename=artifact_name)
