# ReleaseKit — учебное приложение для ЛР №8

Простой список задач (Kotlin, XML views), на котором отрабатывается весь цикл выпуска:
подготовка → подпись → публикация → автоматизация → мониторинг → поддержка.

## Открыть в Android Studio
1. `File → Open` → папка проекта. Дождитесь Gradle Sync (нужен JDK 17 — встроенный в Studio подходит).
2. Скопируйте `local.properties.example` в `local.properties` (Studio сам добавит `sdk.dir`).
3. Запуск: выберите вариант сборки `debug` в **Build Variants** и нажмите Run.
4. Пакет `com.example.releasekit` замените на свой (`namespace` и `applicationId` в `app/build.gradle`,
   + `Refactor → Rename` для пакета) — иначе Play Console скажет, что имя пакета занято.

## Типы сборки (задание 3)
| Тип | applicationId | Название / иконка | API | Тест-крэш |
|---|---|---|---|---|
| debug | `.debug` | ReleaseKit Dev / оранжевая | `API_URL_DEBUG` | да |
| staging | `.staging` | ReleaseKit Staging / синяя | `API_URL_STAGING` | да (с R8, для проверки mapping) |
| release | основной | ReleaseKit / зелёная | `API_URL_RELEASE` | нет |

## Основные команды
```bash
./gradlew testDebugUnitTest            # unit-тесты
./gradlew ktlintCheck detekt lintDebug # линтеры
./gradlew assembleDebug                # debug APK
./gradlew assembleRelease bundleRelease  # подписанные APK и AAB (нужны секреты)
scripts/measure-size.sh                # размер до/после R8 + mapping.txt
scripts/tag-release.sh                 # git-тег v<версия> → запускает release.yml
```

## Firebase (задание 13)
Создайте проект в Firebase Console, добавьте приложения для **каждого** applicationId
(`.debug`, `.staging` и основной), скачайте `google-services.json`, положите в `app/`.
Файл в `.gitignore`. Без него приложение собирается и работает, просто без Firebase.

## Секреты
Никогда не коммитятся: `local.properties`, `*.jks`, `google-services.json`, `play-service-account.json`.
В CI — GitHub Secrets: `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`,
`PLAY_SERVICE_ACCOUNT_JSON`, `GOOGLE_SERVICES_JSON_BASE64`, `ANALYTICS_KEY`.

## Документы по заданиям
| № | Документ |
|---|---|
| 1 | docs/01_checklist.md |
| 2 | docs/02_versioning.md |
| 3–4 | docs/03_04_variants_and_r8.md |
| 5 | docs/05_signing.md |
| 6 | docs/06_distribution_install.md |
| 7 | docs/07_publishing.md |
| 8 | docs/08_store_listing.md, docs/privacy-policy.md |
| 9 | CHANGELOG.md, app/src/main/play/release-notes |
| 10–11 | .github/workflows/ci.yml, release.yml |
| 12 | docs/12_rollout.md |
| 13 | docs/13_monitoring.md |
| 14 | docs/14_hotfix_and_rollback.md |
| 15 | docs/15_support.md, .github/ISSUE_TEMPLATE |
