#!/usr/bin/env bash
# Создаёт аннотированный git-тег v<VERSION_NAME> из version.properties и пушит его.
# Пуш тега v* запускает .github/workflows/release.yml
set -euo pipefail
cd "$(dirname "$0")/.."
VERSION=$(grep '^VERSION_NAME=' version.properties | cut -d= -f2 | tr -d '[:space:]')
if [ -n "$(git status --porcelain)" ]; then echo "Есть незакоммиченные изменения"; exit 1; fi
git tag -a "v$VERSION" -m "Release $VERSION"
git push origin "v$VERSION"
echo "Тег v$VERSION отправлен"
