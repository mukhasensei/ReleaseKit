# Задание 7. Публикация в магазине

Рекомендуемый канал для лабораторной: **Google Play → Internal testing** (проверка почти мгновенная) или закрытое тестирование;
альтернативы — RuStore (бесплатно, модерация 1–3 дня) или GitHub Releases (без модерации).

## Google Play Console — шаги (делайте скриншот каждого)
1. Разовый взнос 25 $ и верификация аккаунта разработчика. (Скриншот: Dashboard)
2. **Create app**: название, язык, App/Game, Free/Paid, принять декларации. (Скриншот)
3. **Set up your app** (Dashboard): Privacy policy (ссылка из docs/privacy-policy.md), App access, Ads, Content rating (IARC),
   Target audience, Data safety, Government apps и т.д. — всё, что в списке «To do».
4. **Testing → Internal testing → Create new release**. Включите Play App Signing (по умолчанию).
5. Загрузите `app-release.aab` (скриншот: загрузка). Play покажет versionCode, размер и предупреждения.
6. Release notes: вставьте тексты из `app/src/main/play/release-notes/*/default.txt`.
7. **Save → Review release → Start rollout to Internal testing**. (Скриншот: статус «Available to internal testers»)
8. Testers → создайте список e-mail тестеров, скопируйте ссылку-приглашение и установите приложение с реального устройства.
9. Скриншоты: «Release dashboard» (статус проверки), «Publishing overview» (Changes in review / Ready to publish).

Для production в новых личных аккаунтах Google требует **закрытое тестирование: минимум 12 тестеров на протяжении 14 дней**
перед первым выходом в production — проверьте актуальные условия в Play Console (Help → Test requirements for new personal accounts).

## RuStore (если вместо Google Play)
Консоль: console.rustore.ru → Приложения → Добавить → загрузить **APK** (подписанный вашим ключом) → заполнить карточку →
отправить на модерацию → «Опубликовать после модерации». Скриншоты: загрузка APK, карточка, статус модерации.

## GitHub Releases (минимальный вариант)
`release.yml` сам создаёт Release и прикладывает APK/AAB при пуше тега `v*`.
Скриншот: страница релиза со ссылкой на APK.

## Типичные причины отклонения (запишите, что случилось у вас, для выводов)
- Нет ссылки на политику конфиденциальности / она недоступна.
- Data Safety не совпадает с реальным поведением (например, указано «не собираем данные», а Crashlytics собирает).
- `targetSdk` ниже требований Google Play.
- Дублируется versionCode — Play сообщит «Version code N has already been used». Лечится повышением PATCH.
- Ошибка «You uploaded a debuggable APK/AAB» либо подпись debug-ключом — проверьте, что в CI заданы секреты keystore.
