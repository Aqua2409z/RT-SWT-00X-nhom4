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
- `baseline_only`: runs precheck and the clean baseline build gate, then stops before AgoneTest/GPT/EvoSuite generation.
- `report_only`: builds reports from existing AgoneTest outputs.
- `full_run`: runs a baseline build gate first, then runs GPT-4o-mini and EvoSuite through AgoneTest only if every sampled class passes baseline, then exports CSV/XLSX reports.

Artifacts are written under `results/runs/{run_id}`. API keys are never returned by the API or written to exported reports.

Key artifacts:

- `baseline_build.csv`: clean build/test-compile result per project/module before generation.
- `baseline_passed_classes.csv`: rows that passed baseline, written only when the strict baseline gate fails.
- `generation_classes.csv`: exact class rows sent to AgoneTest. In strict baseline-gate failures this file is empty.
- `metrics_long.csv`: long metrics table; generated-test failures use zero-filled strict metrics.
- `generated_tests_manifest.csv` and `generated_tests.zip`: per-run copies of generated GPT/EvoSuite test classes.
