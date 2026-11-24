# Задание 1. Аудит готовности к релизу

Отметьте `[x]` по факту и приложите скриншоты/ссылки в отчёт.

## Функциональность и качество
- [ ] Все заявленные функции работают: добавить, удалить, сортировать, поделиться, проверка обновлений
- [ ] Unit-тесты зелёные: `./gradlew testDebugUnitTest`
- [ ] Android Lint без ошибок: `./gradlew lintDebug`
- [ ] Известные баги записаны в issue tracker (список: ______)
- [ ] Нет крэшей при повороте экрана, свёрнутом/восстановленном приложении, пустом списке

## Тестирование на устройствах
| Устройство / эмулятор | Android | Результат |
|---|---|---|
| Pixel 6 (эмулятор) | 14 (API 34) | |
| Pixel 8 (эмулятор) | 16 (API 36) | |
| Реальное устройство | ___ | |
| Минимальная версия (эмулятор) | 8.0 (API 26) | |

## Разрешения (permissions)
| Разрешение | Зачем | Опасное? |
|---|---|---|
| `INTERNET` | Remote Config, Crashlytics, проверка обновлений | нет (normal) |
| `ACCESS_NETWORK_STATE`, `WAKE_LOCK`… | добавляются библиотеками Firebase автоматически | нет |

Проверка итогового манифеста: Android Studio → `AndroidManifest.xml` → вкладка **Merged Manifest**.
Опасных (runtime) разрешений нет, поэтому диалогов запроса нет.

## Сторонние библиотеки и лицензии
| Библиотека | Лицензия |
|---|---|
| AndroidX (core-ktx, appcompat) | Apache 2.0 |
| Material Components | Apache 2.0 |
| Google Play app-update | Android Software Development Kit License / Play Core terms |
| Firebase (Crashlytics, Analytics, Remote Config) | Apache 2.0 + Firebase Terms of Service |
| JUnit (только тесты) | EPL 1.0 |

Совместимо с распространением в закрытом (proprietary) приложении; требуется указать атрибуцию в разделе «О приложении»/README
(при необходимости — через `com.google.android.gms:play-services-oss-licenses`).

## Секреты и отладка
- [ ] В коде нет ключей/паролей: `git grep -n -i -E "password|secret|api[_-]?key|token"`
- [ ] Секреты только в `local.properties` / env / CI Secrets; они в `.gitignore`
- [ ] `git log -p | grep -i -E "BEGIN (RSA|PRIVATE)"` — пусто (в истории нет ключей)
- [ ] Нет `Log.d/Log.v` с персональными данными: `git grep -n "Log\.[dv](" -- app/src/main`
- [ ] Кнопка тест-крэша скрыта в release (`ENABLE_TEST_CRASH=false`)
- [ ] `debuggable` выключен в release (по умолчанию)
- [ ] `allowBackup="false"` (или настроены правила бэкапа)
- [ ] `targetSdk` соответствует требованию Google Play (см. docs/15_support.md, часть Б)
