# RBL-4 Experiment Tool

Local FastAPI + React dashboard for the RBL-4 CLASSES2TEST experiment.

## Backend

From `data2/class2test`:

```bash
pip install -r AgoneTest/requirements.txt
pip install -r experiment_tool/requirements.txt
python -m uvicorn experiment_tool.app:app --host 127.0.0.1 --port 8000 --reload
```

## Frontend

From `data2/class2test/react-ui`:

```bash
npm install
npm run dev
```

Open the Vite local URL, usually `http://127.0.0.1:5173`.

## Run Modes

- `dry_run`: stages sample and writes validation/report skeletons without OpenAI, EvoSuite, JaCoCo, or PIT execution.
- `report_only`: builds reports from existing AgoneTest outputs.
- `full_run`: runs a baseline build gate first, then runs GPT-4o-mini and EvoSuite through AgoneTest only for baseline-passing classes, then exports CSV/XLSX reports.

Artifacts are written under `results/runs/{run_id}`. API keys are never returned by the API or written to exported reports.

Key artifacts:

- `baseline_build.csv`: clean build/test-compile result per project/module before generation.
- `generation_classes.csv`: exact class rows sent to AgoneTest after the baseline gate.
- `metrics_long.csv`: long metrics table; baseline failures use `fail_stage=repo_baseline_failed`.
- `generated_tests_manifest.csv` and `generated_tests.zip`: per-run copies of generated GPT/EvoSuite test classes.
