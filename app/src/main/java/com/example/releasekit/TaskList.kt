package com.example.releasekit

/** Чистая логика списка задач (без Android-зависимостей — удобно тестировать). */
class TaskList(initial: List<String> = emptyList()) {
    private val _items = initial.toMutableList()
    val items: List<String> get() = _items.toList()

    /** Добавляет задачу. Возвращает false для пустой строки и дубликатов (без учёта регистра). */
    fun add(title: String): Boolean {
        val t = title.replace('\n', ' ').trim()
        if (t.isEmpty() || _items.any { it.equals(t, ignoreCase = true) }) return false
        _items.add(t)
        return true
    }

    fun removeAt(index: Int): Boolean {
        if (index !in _items.indices) return false
        _items.removeAt(index)
        return true
    }

    /** Новая функция версии 1.1.0: сортировка по алфавиту. */
    fun sortAlphabetically() {
        _items.sortBy { it.lowercase() }
    }

    fun toShareText(): String = _items.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n")

    fun serialize(): String = _items.joinToString("\n")

    companion object {
        fun deserialize(raw: String?): TaskList = TaskList(raw?.split("\n")?.filter { it.isNotBlank() } ?: emptyList())
    }
}
