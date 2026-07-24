# External Data V3 artifacts

Large binary artifacts are intentionally excluded from Git.

## Prepared delivery

`DELIVERY_MANIFEST.json` and `DELIVERY_SHA256SUMS.csv` describe
`V3_BUILD_HANDOFF_20260723_r1`, which includes:

- `V3_BUILD_BUNDLE.tar.gz`;
- `classes2test-v3-build-handoff-1.0.tar`;
- import and execution instructions;
- the delivery verifier.

`verify_delivery.py` validates the manifest, checksums, and archive membership.
It does not invoke Maven or Gradle and does not build the 30 repositories.

## Scientific status

The delivery manifest is currently marked
`CANDIDATE_NOT_CONFIRMED_48_OF_48`. This status must remain unchanged until all
48 recipes pass under the recorded receiving-environment fingerprint. A
successful packaging check or `--check-only` run is not a 48/48 confirmation.

## Download location

No archive URL has been published in this repository. After the team chooses
Google Drive, OneDrive, OSF, Zenodo, or another suitable release service, add:

- the stable download URL;
- upload date;
- file size;
- SHA-256 matching `DELIVERY_SHA256SUMS.csv`;
- access conditions and artifact version.

Do not commit TAR/ZIP files, Docker images, or third-party repository checkouts
directly to `main`.
