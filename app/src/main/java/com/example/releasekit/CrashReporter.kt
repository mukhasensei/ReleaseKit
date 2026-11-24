package com.example.releasekit

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashReporter {
    fun init() {
        try {
            FirebaseCrashlytics.getInstance().apply {
                setCustomKey("build_type", BuildConfig.BUILD_TYPE)
                setCustomKey("version_code", BuildConfig.VERSION_CODE)
            }
        } catch (ignored: IllegalStateException) {
            // Firebase не настроен — сборка работает без отчётов о сбоях
        }
    }

    /** Задание 13: тестовый крэш. Кнопка видна только в debug/staging (BuildConfig.ENABLE_TEST_CRASH). */
    fun crashForTest(): Nothing {
        throw IllegalStateException("Test crash from ReleaseKit ${BuildConfig.VERSION_NAME}")
    }
}
