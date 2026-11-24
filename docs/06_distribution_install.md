# Задание 6. Формат поставки

```bash
./gradlew bundleRelease     # app/build/outputs/bundle/release/app-release.aab
./gradlew assembleRelease   # app/build/outputs/apk/release/app-release.apk
```

| | AAB (Android App Bundle) | APK |
|---|---|---|
| Назначение | загрузка в Google Play | прямая установка, RuStore, AppGallery, GitHub Releases |
| Устанавливается напрямую | нет | да |
| Размер скачивания | меньше: Play собирает APK под устройство (язык, плотность, ABI) | универсальный, больше |
| Подпись | upload key, финально подписывает Google | ваш ключ |
| Требование Google Play | обязателен для новых приложений | не принимается |

Проверка AAB локально (по желанию): `bundletool build-apks --bundle=app-release.aab --output=out.apks --ks=release.jks --ks-key-alias=upload` и
`bundletool install-apks --apks=out.apks`.

## Инструкция по установке APK (для пользователей)
1. Скачайте `app-release.apk` со страницы **GitHub Releases** (или из RuStore).
2. Откройте файл. При первой установке Android предложит разрешить установку из этого источника:
   - Android 8+: **Настройки → Приложения → Специальный доступ → Установка неизвестных приложений** → выберите браузер/«Файлы» → включите «Разрешить из этого источника».
   - Android 7 и ниже: **Настройки → Безопасность → Неизвестные источники**.
3. Нажмите «Установить», затем «Открыть».
4. После установки отключите разрешение для этого источника.
5. Проверка подлинности: сравните SHA-256 (`sha256sum app-release.apk`) с указанным на странице релиза.

Установка через ADB (для разработчика): `adb install -r app-release.apk`.
Обновление поверх установленной версии возможно, только если подпись совпадает и versionCode выше.
