#!/usr/bin/env bash
# Сохраняет mapping.txt под номером версии (нужен для расшифровки крэшей).
set -euo pipefail
cd "$(dirname "$0")/.."
VERSION=$(grep '^VERSION_NAME=' version.properties | cut -d= -f2 | tr -d '[:space:]')
DEST="artifacts/mapping/$VERSION"
mkdir -p "$DEST"
cp app/build/outputs/mapping/release/mapping.txt "$DEST/mapping.txt"
echo "mapping.txt сохранён в $DEST"
