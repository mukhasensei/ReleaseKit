# Задание 8. Карточка приложения и документы

Всё лежит в `app/src/main/play/listings/<locale>/` — это же структура Gradle Play Publisher
(`./gradlew publishReleaseListing`) и ручной ввод в консоль.

| Элемент | Требование | Где у нас |
|---|---|---|
| Название | до 30 символов | `title.txt` («ReleaseKit Tasks» / «ReleaseKit: задачи») |
| Краткое описание | до 80 символов | `short-description.txt` |
| Полное описание | до 4000 символов | `full-description.txt` |
| Иконка | PNG 512×512, до 1 МБ | `graphics/icon/icon.png` |
| Feature graphic | PNG/JPEG 1024×500 | `graphics/feature-graphic/feature.png` |
| Скриншоты | минимум 2 (для рекомендаций — 4+), 16:9 или 9:16, стороны 320–3840 px | `graphics/phone-screenshots/` — **сделайте сами** |

## Как сделать скриншоты (минимум 4)
Запустите release/staging сборку на эмуляторе Pixel (1080×1920 или 1080×2400) и снимите состояния:
1. пустой список + строка сборки; 2. список с 5–6 задачами; 3. после сортировки А–Я; 4. диалог «Поделиться».
```bash
adb exec-out screencap -p > 1.png
```
Положите файлы как `phone-screenshots/1.png … 4.png` (или загрузите в консоль вручную).

## Возрастной рейтинг
В консоли: **Policy → App content → Content rating** — анкета IARC. Для приложения без насилия, азартных игр,
UGC и рекламы итог: **3+ / PEGI 3 / Everyone**, в России — **0+**. Ответы в анкете должны соответствовать приложению.

## Политика конфиденциальности
Текст — `docs/privacy-policy.md`. Должна быть доступна по **публичной HTTPS-ссылке**:
GitHub → Settings → Pages → Source: *Deploy from a branch*, ветка `main`, папка `/docs` →
ссылка вида `https://<user>.github.io/<repo>/privacy-policy`. Эту ссылку вставьте в Play Console (App content → Privacy policy)
и в карточку RuStore. Подставьте свой e-mail и дату.

## Форма Data Safety (черновик ответов)
Заполняйте по реальному составу SDK — проверьте после добавления/удаления библиотек.

| Вопрос | Ответ для ReleaseKit |
|---|---|
| Собирает ли приложение пользовательские данные? | Да |
| Передаются ли данные третьим лицам? | Нет (Firebase — сервис-провайдер, обрабатывающий данные от вашего имени; сверьтесь с актуальными подсказками Google) |
| Данные шифруются при передаче? | Да (HTTPS) |
| Можно ли запросить удаление данных? | Да, письмом на контактный e-mail (указать в политике) |
| Типы данных | **App info and performance → Crash logs; Diagnostics.** **App activity → App interactions** (аналитика). **Device or other IDs** (идентификатор установки Firebase) |
| Цель | Analytics, App functionality (crash reports), Developer communications — не нужны |
| Обязательно ли для работы? | Нет (необязательные: можно отключить в Firebase, если добавите переключатель) |
| Задачи пользователя | Хранятся только на устройстве, **не** собираются → указывать не нужно |
| Разрешения | Только INTERNET |

Если подключите AppMetrica/Sentry — добавьте их данные и в политику, и в Data Safety.
