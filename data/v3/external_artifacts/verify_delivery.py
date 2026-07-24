#!/usr/bin/env python3
"""Verify an already-created Data V3 delivery without extracting or loading it."""

from __future__ import annotations

import argparse
import csv
import hashlib
import json
import sys
import tarfile
from pathlib import Path, PurePosixPath


class VerificationError(RuntimeError):
    """Raised for a controlled delivery verification failure."""


def sha256_file(path: Path, chunk_size: int = 8 * 1024 * 1024) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        while True:
            chunk = handle.read(chunk_size)
            if not chunk:
                break
            digest.update(chunk)
    return digest.hexdigest()


def read_checksums(path: Path) -> list[dict[str, str]]:
    if not path.is_file():
        raise VerificationError(f"Checksum manifest is missing: {path}")
    with path.open("r", encoding="utf-8-sig", newline="") as handle:
        rows = list(csv.DictReader(handle))
    if not rows:
        raise VerificationError("Checksum manifest is empty")
    return rows


def inspect_archive(path: Path, bundle_root: str) -> int:
    required = {
        f"{bundle_root}/data_v3/results/RUN_READY",
        f"{bundle_root}/data_v3/successful_repos_manifest.csv",
        f"{bundle_root}/data_v3/class_sampling_manifest_final_seed42.csv",
        f"{bundle_root}/data_v3/build_recipes_portable.csv",
        f"{bundle_root}/data_v3/repos/successful",
        f"{bundle_root}/research_pipeline_v3/config_v3.yaml",
        f"{bundle_root}/v3_build_handoff/Dockerfile",
        f"{bundle_root}/v3_build_handoff/compose.yaml",
        f"{bundle_root}/v3_build_handoff/scripts/replay_builds.py",
    }
    found: set[str] = set()
    count = 0
    try:
        with tarfile.open(path, mode="r:gz") as archive:
            for member in archive:
                count += 1
                pure = PurePosixPath(member.name)
                if pure.is_absolute() or ".." in pure.parts:
                    raise VerificationError(f"Unsafe archive member: {member.name}")
                normalized = member.name.rstrip("/")
                if normalized in required:
                    found.add(normalized)
    except (OSError, tarfile.TarError) as error:
        raise VerificationError(f"Cannot inspect archive {path}: {error}") from error
    missing = sorted(required - found)
    if missing:
        raise VerificationError(
            "Archive is missing required entries: " + ", ".join(missing)
        )
    return count


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(
        description="Verify delivery SHA-256 values and archive structure."
    )
    parser.add_argument(
        "delivery",
        nargs="?",
        type=Path,
        default=Path.cwd(),
        help="Delivery directory (default: current directory)",
    )
    args = parser.parse_args(argv)
    try:
        delivery = args.delivery.resolve()
        if not delivery.is_dir():
            raise VerificationError(f"Delivery directory is missing: {delivery}")
        manifest_path = delivery / "DELIVERY_MANIFEST.json"
        try:
            manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
        except (OSError, json.JSONDecodeError) as error:
            raise VerificationError(f"Cannot read delivery manifest: {error}") from error
        if manifest.get("schema_version") != 1:
            raise VerificationError("Unsupported delivery manifest schema")

        rows = read_checksums(delivery / "DELIVERY_SHA256SUMS.csv")
        for row in rows:
            relative = PurePosixPath(row.get("path", ""))
            if relative.is_absolute() or ".." in relative.parts:
                raise VerificationError(f"Unsafe checksum path: {relative}")
            path = delivery / Path(*relative.parts)
            if not path.is_file():
                raise VerificationError(f"Delivery file is missing: {path}")
            observed_size = path.stat().st_size
            expected_size = int(row["size_bytes"])
            if observed_size != expected_size:
                raise VerificationError(
                    f"Size mismatch for {relative}: expected {expected_size}, "
                    f"observed {observed_size}"
                )
            observed_hash = sha256_file(path)
            if observed_hash.lower() != row["sha256"].lower():
                raise VerificationError(f"SHA-256 mismatch for {relative}")

        archive_name = str(manifest["archive_filename"])
        members = inspect_archive(
            delivery / archive_name, str(manifest["bundle_root_name"])
        )
        print(
            f"DELIVERY VERIFY PASS: files={len(rows)}, archive_members={members}, "
            f"status={manifest.get('status')}"
        )
        return 0
    except VerificationError as error:
        print(f"STOP: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
