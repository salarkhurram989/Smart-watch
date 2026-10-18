# SALARX Haylou Connect

A native Android companion-app prototype for the Haylou Smart Watch 2 (LS02), targeting the Samsung Galaxy Tab 10.1 SHW-M380K running Android 4.0.4 (API 15).

## Current status

This repository is the **source project**, not a prebuilt APK yet. GitHub Actions is configured to build an APK and publish it as a workflow artifact.

The app includes a HAYLOU FUN-inspired original dashboard and a reporting interface. It does not fake watch measurements.

### Important compatibility limitation

Android 4.0.4 does not provide the standard Android BLE GATT API (introduced in Android 4.3/API 18). The Haylou LS02 uses BLE, so direct communication from this tablet is not confirmed and may be impossible without a compatible legacy BLE implementation. Until the transport/protocol is implemented and tested against a real LS02, health data, battery, time sync, and notifications are marked unavailable rather than fabricated.

## Build an APK

1. Open the **Actions** tab.
2. Select **Build Android APK**.
3. Run the workflow (or push a commit to `main`).
4. Open the completed run and download the `SALARX-Haylou-Connect-debug-apk` artifact.
5. Extract the ZIP and copy the APK to the tablet.

The workflow must complete successfully before an APK artifact exists.

## Project details

- Native Java Android app
- Minimum SDK: 15 (Android 4.0.3+)
- Local-only UI and report history prototype
- No cloud account or phone bridge required by the UI
- No claim of real watch connectivity until validated on hardware

## Reference

LS02 reverse-engineering research: https://github.com/XorTroll/ls02-re
