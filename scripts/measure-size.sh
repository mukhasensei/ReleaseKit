#!/usr/bin/env bash
# Задание 4: сравнение размера release APK без/с R8 + сохранение mapping.txt
set -euo pipefail
cd "$(dirname "$0")/.."
OUT=artifacts/size
mkdir -p "$OUT"

./gradlew clean assembleRelease -PnoMinify
cp app/build/outputs/apk/release/app-release.apk "$OUT/app-no-r8.apk"

./gradlew clean assembleRelease
cp app/build/outputs/apk/release/app-release.apk "$OUT/app-r8.apk"

BEFORE=$(stat -c %s "$OUT/app-no-r8.apk")
AFTER=$(stat -c %s "$OUT/app-r8.apk")
echo "Без R8: $BEFORE байт"
echo "С R8:   $AFTER байт"
echo "Экономия: $(( (BEFORE - AFTER) * 100 / BEFORE ))%"
./scripts/save-mapping.sh
