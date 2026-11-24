# Задание 3. Варианты сборки и конфигурации

Настроено в `app/build.gradle` (блок `buildTypes`):

| | debug | staging | release |
|---|---|---|---|
| applicationId | `…releasekit.debug` | `…releasekit.staging` | `…releasekit` |
| Название | ReleaseKit Dev | ReleaseKit Staging | ReleaseKit |
| Иконка (фон) | оранжевый | синий | зелёный (`src/<type>/res/values/ic_launcher_background.xml`) |
| API_BASE_URL | `API_URL_DEBUG` | `API_URL_STAGING` | `API_URL_RELEASE` |
| R8 | выкл | вкл | вкл |
| Подпись | debug | debug | release keystore |

Разные applicationId позволяют держать все три сборки на одном телефоне одновременно.
Если нужны разные «окружения» ортогонально типам сборки (free/paid), добавляют `productFlavors`.

Секреты читаются функцией `secret(key, fallback)`: сначала переменная окружения (CI), затем `local.properties`
(локально), затем безопасное значение по умолчанию. Файл `local.properties` в `.gitignore`.
Пример — `local.properties.example`.

Проверка: `Build → Select Build Variant`, затем запустить и посмотреть строку сборки на главном экране
(applicationId, versionName, API).

# Задание 4. Оптимизация релизной сборки

Включено для release и staging:
```groovy
minifyEnabled true          // R8: удаление неиспользуемого кода + обфускация
shrinkResources true        // удаление неиспользуемых ресурсов
proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
```
`proguard-rules.pro` сохраняет `SourceFile`/`LineNumberTable`, чтобы в стектрейсе были номера строк.

## Измерение
```bash
scripts/measure-size.sh
```
Скрипт собирает release без R8 (`-PnoMinify`) и с R8, печатает размеры и сохраняет `mapping.txt`
в `artifacts/mapping/<версия>/`. Занесите результат в таблицу:

| Сборка | Размер APK, байт | Размер, КБ |
|---|---|---|
| без R8 | | |
| с R8 + shrinkResources | | |
| Экономия | | % |

(В маленьком приложении выигрыш в основном за счёт неиспользуемых кусков Firebase/Material; ожидайте десятки процентов.)

## Проверка работоспособности после R8
Установить `app-r8.apk` (`adb install -r artifacts/size/app-r8.apk`) и пройти сценарий: добавить, удалить, сортировать,
поделиться, проверить обновление, перезапустить приложение (данные сохранились).
Просмотр обфускации: Android Studio → `Build → Analyze APK…` → `classes.dex` (имена классов вида `a.b.c`).

## mapping.txt
Лежит в `app/build/outputs/mapping/release/mapping.txt`. **Хранить для каждой выпущенной версии**
(`scripts/save-mapping.sh`; CI прикладывает его к артефактам релиза). Без него крэши из прод-сборки не расшифровать.
Crashlytics подхватывает mapping автоматически (плагин `com.google.firebase.crashlytics`); в Play Console
mapping загружается вместе с AAB (в AAB он включается автоматически при R8).
