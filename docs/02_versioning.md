# Задание 2. Версионирование

## Правило
Версия хранится в **одном месте** — `version.properties` (`VERSION_NAME=MAJOR.MINOR.PATCH`).
`versionName` берётся из файла как есть, `versionCode` вычисляется в `app/build.gradle`:

```
versionCode = MAJOR * 10000 + MINOR * 100 + PATCH
```
| versionName | versionCode |
|---|---|
| 0.1.0 | 100 |
| 1.0.0 | 10000 |
| 1.1.0 | 10100 |
| 1.2.0 | 10200 |
| 1.2.1 | 10201 |

Ограничения: MINOR и PATCH < 100 (сборка падает с понятной ошибкой). versionCode должен только расти и быть
уникальным для каждой загрузки в Play — повторно загрузить ту же версию нельзя: поднимайте PATCH.
Для iOS эквивалент — `CFBundleShortVersionString` (SemVer) и `CFBundleVersion` (build number).

## Когда повышается
- **MAJOR** — несовместимые изменения: удалена/изменена функциональность, смена формата хранения данных без миграции,
  повышение minSdk, который отсекает часть пользователей.
- **MINOR** — новая функциональность без поломки старой (сортировка, «Поделиться»).
- **PATCH** — исправления ошибок, hotfix, обновление зависимостей без изменения поведения.
- Суффиксы `-debug`, `-staging` добавляются автоматически к versionName (не к versionCode).
- Предрелизы: `1.3.0-rc1` (в `version.properties`; суффикс отбрасывается при расчёте versionCode).

## Git-теги
```bash
# 0.1.0 — прототип
echo "VERSION_NAME=0.1.0" > version.properties && git commit -am "Release 0.1.0"
git tag -a v0.1.0 -m "Prototype 0.1.0"
# 1.0.0 — первый публичный релиз
echo "VERSION_NAME=1.0.0" > version.properties && git commit -am "Release 1.0.0"
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin main --tags
```
Внимание: пуш тега `v*` запускает `release.yml` (публикация в internal). Для 0.1.0 создайте тег локально и пушьте
его только если секреты CI уже настроены.
