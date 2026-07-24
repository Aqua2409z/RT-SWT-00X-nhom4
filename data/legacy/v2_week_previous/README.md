# Legacy sampling snapshot

The files in this directory were moved unchanged from the former `data/` root
so that they cannot be mistaken for Data V3 results.

This historical snapshot reports 33 repositories, 300 main classes, and 58
backup classes. It also contains build commands with creator-machine absolute
paths and pilot rows still marked `PENDING`. Its sampling methodology uses CC
for eligibility and allocation, unlike the frozen V3 protocol.

Do not use this snapshot for:

- GPT or EvoSuite inputs;
- dataset statistics in the current paper;
- replaying the 30 Data V3 repositories;
- replacing the canonical manifests under `../../v3/`.

The original files include Vietnamese historical documentation. They remain
byte-for-byte unchanged because they are immutable provenance, not current
project documentation.
