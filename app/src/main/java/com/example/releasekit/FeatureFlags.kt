package com.example.releasekit

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

/**
 * Feature flags и kill-switch через Firebase Remote Config.
 * Если Firebase не настроен (нет google-services.json) — работают значения по умолчанию.
 */
object FeatureFlags {
    const val SORT_TASKS = "sort_tasks"
    const val SHARE_TASKS = "share_tasks"
    const val MIN_VERSION_CODE = "min_supported_version_code"

    private val defaults: Map<String, Any> =
        mapOf(
            SORT_TASKS to true,
            SHARE_TASKS to true,
            MIN_VERSION_CODE to 0L,
        )

    private var remote: FirebaseRemoteConfig? = null

    fun init(onUpdated: () -> Unit) {
        remote =
            try {
                FirebaseRemoteConfig.getInstance().also { cfg ->
                    val settings =
                        FirebaseRemoteConfigSettings.Builder()
                            .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0L else FETCH_INTERVAL_SEC)
                            .build()
                    cfg.setConfigSettingsAsync(settings)
                    cfg.setDefaultsAsync(defaults)
                    cfg.fetchAndActivate().addOnCompleteListener { onUpdated() }
                }
            } catch (ignored: IllegalStateException) {
                onUpdated() // Firebase не настроен: работаем на значениях по умолчанию
                null
            }
    }

    fun isEnabled(key: String): Boolean = remote?.getBoolean(key) ?: (defaults[key] as? Boolean ?: false)

    fun minSupportedVersionCode(): Long = remote?.getLong(MIN_VERSION_CODE) ?: 0L

    private const val FETCH_INTERVAL_SEC = 3600L
}
