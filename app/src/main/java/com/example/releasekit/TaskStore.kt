package com.example.releasekit

import android.content.Context

/** Хранение списка задач локально (данные пользователя не покидают устройство). */
class TaskStore(context: Context) {
    private val prefs = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)

    fun load(): TaskList = TaskList.deserialize(prefs.getString(KEY, null))

    fun save(list: TaskList) {
        prefs.edit().putString(KEY, list.serialize()).apply()
    }

    private companion object {
        const val KEY = "items"
    }
}
