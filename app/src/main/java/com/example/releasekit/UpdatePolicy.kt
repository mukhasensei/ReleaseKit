package com.example.releasekit

enum class UpdateAction { NONE, FLEXIBLE, IMMEDIATE }

/**
 * Решает, какое обновление показать пользователю (задание 14: замена «отката»).
 *
 * @param currentVersionCode versionCode установленной сборки
 * @param minSupportedVersionCode минимальная допустимая версия из Remote Config (0 = ограничения нет)
 * @param availableVersionCode версия в Google Play или null, если обновления нет
 * @param priority приоритет обновления (0..5), задаётся при публикации через Play Developer API
 */
object UpdatePolicy {
    fun decide(
        currentVersionCode: Int,
        minSupportedVersionCode: Int,
        availableVersionCode: Int?,
        priority: Int,
    ): UpdateAction =
        when {
            currentVersionCode < minSupportedVersionCode -> UpdateAction.IMMEDIATE
            availableVersionCode == null || availableVersionCode <= currentVersionCode -> UpdateAction.NONE
            priority >= HIGH_PRIORITY -> UpdateAction.IMMEDIATE
            else -> UpdateAction.FLEXIBLE
        }

    private const val HIGH_PRIORITY = 4
}
