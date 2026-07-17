# CLASSES2TEST Pilot Samples

This bundle now contains a deterministic 60-class pilot generated from the balanced 300-row seed-42 manifest.

## Contents

- `output/pilot_60_classes.csv`: 60 focal/test rows for the recommended pilot run.
- `output/pilot_60_backups.csv`: unique backup rows available for replacement analysis.
- `output/classes_main.csv`: all 300 main rows for the final full run.
- `metadata/pilot_60_selection_report.json`: selection audit and distributions.
- `repos/<repo_id>/`: source checkout for each represented repository.
- `metadata/build_recipes.jsonl`: validated compile recipes.

## Pilot summary

- Selected rows: 60
- Distinct repositories: 33
- Duplicate `repo_id + focal_path + test_path` pairs: 0
- CC distribution: {2: 6, 3: 8, 4: 3, 5: 4, 6: 5, 7: 7, 8: 2, 9: 3, 10: 4, 11: 3, 12: 8, 13: 5, 14: 2}

Regenerate the files with:

```powershell
python scripts/create_rbl4_samples.py
```

## Important

Run `output/pilot_60_classes.csv` first, then run `output/classes_main.csv` after the environment is stable.
Do not edit the selected source files when reporting pilot results.
Record the repository ID, focal path, command, exit code and logs for every run.
