#!/usr/bin/env bash
# Создаёт upload key. Храните release.jks и пароли ВНЕ репозитория (см. docs/05_signing.md).
set -euo pipefail
OUT="${1:-release.jks}"
keytool -genkeypair -v -keystore "$OUT" -alias upload \
  -keyalg RSA -keysize 2048 -validity 10000
echo "Готово: $OUT. Сделайте минимум 2 резервные копии в разных местах и закодируйте для CI:"
echo "  base64 -w0 $OUT   # значение для секрета KEYSTORE_BASE64"
