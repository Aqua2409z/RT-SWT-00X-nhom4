from __future__ import annotations

import argparse
import csv
import hashlib
import json
import os
import sys
import time
import traceback
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parent
API_FIELDS = [
    "timestamp_utc",
    "sample_index",
    "class_key",
    "model_requested",
    "model_returned",
    "duration_sec",
    "prompt_chars",
    "completion_chars",
    "prompt_hash_sha256",
    "prompt_tokens",
    "completion_tokens",
    "total_tokens",
    "top_p",
    "max_tokens",
    "status",
    "attempts",
    "error_type",
    "error_message",
]


def utc_now() -> str:
    from datetime import datetime, timezone

    return datetime.now(timezone.utc).isoformat(timespec="seconds")


def append_csv(path: Path, row: dict[str, Any], fieldnames: list[str]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    for attempt in range(10):
        try:
            exists = path.exists()
            with path.open("a", newline="", encoding="utf-8") as f:
                writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
                if not exists:
                    writer.writeheader()
                writer.writerow(row)
            return
        except PermissionError:
            time.sleep(0.1 * (attempt + 1))
    fallback = path.with_name(path.name + ".fallback.csv")
    exists = fallback.exists()
    with fallback.open("a", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
        if not exists:
            writer.writeheader()
        writer.writerow(row)


def append_jsonl(path: Path, row: dict[str, Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    line = json.dumps(row, ensure_ascii=False)
    for attempt in range(10):
        try:
            with path.open("a", encoding="utf-8") as f:
                f.write(line + "\n")
            return
        except PermissionError:
            time.sleep(0.1 * (attempt + 1))
    with path.with_name(path.name + ".fallback.jsonl").open("a", encoding="utf-8") as f:
        f.write(line + "\n")


def message_content(messages: Any) -> str:
    if not isinstance(messages, list):
        return ""
    parts: list[str] = []
    for msg in messages:
        if isinstance(msg, dict):
            parts.append(str(msg.get("content", "")))
    return "\n".join(parts)


def patch_api_logging() -> None:
    sys.path.insert(0, str(ROOT / "AgoneTest"))
    import utils  # type: ignore

    original_completion = utils.completion
    api_log = Path(os.environ.get("RBL4_API_LOG_CSV", "api_log.csv"))
    prompt_log = Path(os.environ.get("RBL4_API_PROMPTS_JSONL", "api_prompts.jsonl"))

    def logged_completion(*args: Any, **kwargs: Any) -> Any:
        started = time.time()
        model_requested = kwargs.get("model") or (args[0] if args else "")
        if model_requested != "evosuite":
            kwargs.setdefault("top_p", 1)
            kwargs.setdefault("max_tokens", 2048)
        messages = kwargs.get("messages") or []
        prompt_text = message_content(messages)
        prompt_sha = hashlib.sha256(prompt_text.encode("utf-8", errors="replace")).hexdigest()
        row = {
            "timestamp_utc": utc_now(),
            "sample_index": os.environ.get("RBL4_SAMPLE_INDEX", ""),
            "class_key": os.environ.get("RBL4_CLASS_KEY", ""),
            "model_requested": model_requested,
            "model_returned": "",
            "duration_sec": "",
            "prompt_chars": len(prompt_text),
            "completion_chars": "",
            "prompt_hash_sha256": prompt_sha,
            "prompt_tokens": "",
            "completion_tokens": "",
            "total_tokens": "",
            "top_p": kwargs.get("top_p", ""),
            "max_tokens": kwargs.get("max_tokens", ""),
            "status": "ERROR",
            "attempts": 0,
            "error_type": "",
            "error_message": "",
        }
        append_jsonl(
            prompt_log,
            {
                "timestamp_utc": row["timestamp_utc"],
                "sample_index": row["sample_index"],
                "class_key": row["class_key"],
                "model_requested": model_requested,
                "prompt_hash_sha256": prompt_sha,
                "messages": messages,
            },
        )
        last_exc: BaseException | None = None
        for attempt in range(1, 4):
            row["attempts"] = attempt
            try:
                response = original_completion(*args, **kwargs)
                content = ""
                if getattr(response, "choices", None):
                    message = response.choices[0].message
                    content = message.get("content", "") if isinstance(message, dict) else getattr(message, "content", "")
                usage = getattr(response, "usage", None)
                row.update(
                    {
                        "model_returned": getattr(response, "model", ""),
                        "duration_sec": round(time.time() - started, 3),
                        "completion_chars": len(content or ""),
                        "prompt_tokens": getattr(usage, "prompt_tokens", ""),
                        "completion_tokens": getattr(usage, "completion_tokens", ""),
                        "total_tokens": getattr(usage, "total_tokens", ""),
                        "status": "OK",
                        "error_type": "",
                        "error_message": "",
                    }
                )
                append_csv(api_log, row, API_FIELDS)
                return response
            except BaseException as exc:
                last_exc = exc
                row.update(
                    {
                        "duration_sec": round(time.time() - started, 3),
                        "error_type": type(exc).__name__,
                        "error_message": str(exc)[:1000],
                    }
                )
                if attempt < 3:
                    time.sleep(2**attempt)
        append_csv(api_log, row, API_FIELDS)
        assert last_exc is not None
        raise last_exc

    utils.completion = logged_completion


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Run AgoneTest for one staged sandbox.")
    parser.add_argument("--sandbox", type=Path, required=True)
    parser.add_argument("--project", required=True)
    parser.add_argument("--model", required=True)
    parser.add_argument("--prompt", required=True)
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    os.chdir(args.sandbox)
    sys.path.insert(0, str(ROOT / "AgoneTest"))
    patch_api_logging()
    try:
        import agone_test  # type: ignore

        agone_test.generate_files(
            test_types=["evosuite", args.model],
            techniques=[args.prompt],
            execution_override=True,
            correct=False,
            specific_project=str(args.project),
        )
        return 0
    except BaseException:
        traceback.print_exc()
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
