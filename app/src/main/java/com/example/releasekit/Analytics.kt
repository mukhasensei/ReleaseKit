package com.example.releasekit

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Analytics {
    fun log(context: Context, event: String, params: Bundle? = null) {
        try {
            FirebaseAnalytics.getInstance(context).logEvent(event, params)
        } catch (ignored: IllegalStateException) {
            // Firebase не настроен
        }
    }
}
