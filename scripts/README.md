# RBL-4 Experiment Tool v2

Bundle này dùng dataset `data_new`:

- `data_new/class_sampling_manifest_final_seed42.csv`: 300 focal class, 30 repo, 10 class/repo.
- `data_new/build_recipes_portable.csv`: 48 build recipe theo `repo_id + scope_key`.
- `compiledrepos` không nằm trong bundle. Đặt ở sibling của `scripts_v2`:

```text
balanced_bundle_300/
  compiledrepos/
    64487983/
    74999474/
    ...
  scripts_v2/
```

Hoặc đặt biến môi trường `RBL4_COMPILED_REPOS`.

## Prompt Chính Thức

Tool v2 khóa prompt gốc AgoneTest zero-shot theo `proposal.md`. Không dùng `java_language_rules`, không thêm project structure/dependencies.

## Chạy CLI

Từ thư mục `scripts_v2`:

```powershell
python rbl4_v2_runner.py --mode dry_run
python rbl4_v2_runner.py --mode baseline_only
python rbl4_v2_runner.py --mode full_run
python rbl4_v2_runner.py --mode full_run --workers 2
```

`dry_run` kiểm tra dataset, recipe, repo/focal file. `baseline_only` chạy build recipe cho 48 scope và map kết quả về 300 class. `full_run` chỉ bắt đầu GPT/EvoSuite nếu toàn bộ 300 class pass baseline.

`--workers N` chạy song song theo focal class sau khi qua baseline gate. Mặc định `N=1` để giữ hành vi cũ; laptop 16 GB RAM nên bắt đầu với `--workers 2`, máy 64 GB RAM/NVMe có thể thử `--workers 6` hoặc `--workers 8`. Các log chung (`phase_log.csv`, `api_log.csv`, `api_prompts.jsonl`) đã có file lock để tránh hỏng CSV/JSONL khi nhiều process ghi đồng thời.

## Chạy UI

Backend:

```powershell
python -m uvicorn experiment_tool.app:app --host 127.0.0.1 --port 8000
```

Frontend:

```powershell
cd react-ui
npm install
npm run dev
```

Mở `http://127.0.0.1:5173`.

## Isolation

Mỗi focal class được copy sang sandbox riêng:

```text
scripts_v2/workspaces/<run_id>/<class_id>/compiledrepos/<repo_id>
```

AgoneTest chỉ nhận một dòng `output/classes.csv` trong sandbox đó. Maven/Gradle vẫn được phép compile module cần thiết, nhưng generated test, `-Dtest`, JaCoCo và PIT target chỉ tương ứng focal class đang xét.

## Kết Quả

Mỗi run nằm ở:

```text
scripts_v2/results/runs/<run_id>
```

Các file chính:

- `preflight_classes.csv`, `preflight_report.json`
- `baseline_scope_build.csv`, `baseline_classes.csv`
- `metrics_long.csv`
- `summary.csv`
- `rq_decisions.csv`
- `generated_failures.csv`
- `api_log.csv`, `api_prompts.jsonl`
- `generated_tests_manifest.csv`, `generated_tests/`
- `manifest.json`, `status.json`, `phase_log.csv`

Không copy `.env` thật vào bundle. Dùng `.env.example` để tạo `.env` riêng trên máy chạy.
