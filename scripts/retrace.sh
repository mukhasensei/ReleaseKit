#!/usr/bin/env bash
# Задание 13: расшифровка обфусцированного стектрейса.
# Использование: scripts/retrace.sh <mapping.txt> <stacktrace.txt>
# retrace входит в Android SDK: cmdline-tools/latest/bin/retrace
set -euo pipefail
"${ANDROID_HOME:?Установите ANDROID_HOME}/cmdline-tools/latest/bin/retrace" "$1" "$2"
