# Задание 13. Мониторинг и аналитика

## Подключение Firebase (Crashlytics + Analytics + Remote Config)
1. console.firebase.google.com → создать проект → добавить Android-приложения для **трёх** applicationId
   (`com.example.releasekit`, `.staging`, `.debug`).
2. Скачать `google-services.json`, положить в `app/` (файл в `.gitignore`; в CI — секрет `GOOGLE_SERVICES_JSON_BASE64`).
3. Пересинхронизировать Gradle: при наличии файла `app/build.gradle` сам применит плагины `google-services` и `crashlytics`.
   Зависимости (`firebase-crashlytics`, `firebase-analytics`, `firebase-config`) уже подключены.
4. Запустить любую сборку, дождаться первого запуска (в консоли Crashlytics появится «Waiting for your app to communicate»).

Если нужен Sentry/AppMetrica вместо Firebase — замените реализацию в `CrashReporter.kt` и `Analytics.kt`, остальной код не меняется.

## Тестовый крэш и расшифровка стектрейса
1. Соберите **staging** (с R8): `./gradlew installStaging` → нажмите «Тестовый крэш» → снова откройте приложение
   (отчёт отправляется при следующем запуске).
2. Firebase → Crashlytics → Issues: найдите `IllegalStateException: Test crash from ReleaseKit …` (скриншот).
3. Crashlytics подставляет mapping автоматически (плагин загружает mapping.txt при сборке staging/release). В списке кадров
   вместо `a.b.c` вы увидите `CrashReporter.crashForTest` (скриншот: «до» — если вручную посмотреть обфусцированный лог,
   «после» — в консоли).
4. Ручная расшифровка (если crash пришёл из Play Console или логов):
```bash
scripts/retrace.sh artifacts/mapping/1.0.0/mapping.txt stacktrace.txt
```
   Mapping должен соответствовать **именно** той сборке (версии), в которой произошёл крэш.

## Оповещения (минимум два)
Firebase → Crashlytics → Alerts (Settings → Integrations/Alerts) / Firebase → Alerts:
| № | Оповещение | Порог / настройка |
|---|---|---|
| 1 | **New fatal issue** (новая критическая ошибка) | включить «New fatal issues» → e-mail |
| 2 | **Regressed issue** (ошибка вернулась после исправления) | включить |
| 3 | **Trending stability issue** / Velocity alert | включить |
| 4 | Падение **crash-free users** ниже 99% | Google Cloud Monitoring или Crashlytics Alerts → порог 99% (для Crashlytics через BigQuery/Cloud Monitoring); альтернатива: ручная проверка в критериях `docs/12_rollout.md` |

Скриншоты: страница Alerts с включёнными оповещениями + тестовое письмо.

## Аналитика
События, логируемые в коде: `task_added`, `tasks_sorted` (`Analytics.kt`). Проверка: Firebase → Analytics → DebugView
(`adb shell setprop debug.firebase.analytics.app com.example.releasekit.debug`).

## Android Vitals (Play Console → Quality → Android vitals)
Данные появляются, когда у приложения достаточно пользователей. Изучить и описать в отчёте:
- **Crash rate** и **User-perceived ANR rate** — сравнить с пороговыми значениями Google (bad behavior threshold);
- **App startup time** (cold / warm / hot) — порог холодного старта: 5 с и более считается медленным;
- также: расход батареи (excessive wakeups), рендеринг (slow frames).
Если данных нет (тестовая дорожка) — зафиксируйте это скриншотом «Not enough data» и опишите, какие метрики вы бы отслеживали.
