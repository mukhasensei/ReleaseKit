package com.example.releasekit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskListTest {
    @Test
    fun add_rejectsBlankAndDuplicates() {
        val list = TaskList()
        assertTrue(list.add("  Buy milk "))
        assertFalse(list.add("buy MILK"))
        assertFalse(list.add("   "))
        assertEquals(listOf("Buy milk"), list.items)
    }

    @Test
    fun removeAt_validatesIndex() {
        val list = TaskList(listOf("a", "b"))
        assertFalse(list.removeAt(5))
        assertTrue(list.removeAt(0))
        assertEquals(listOf("b"), list.items)
    }

    @Test
    fun sortAlphabetically_ignoresCase() {
        val list = TaskList(listOf("b", "A", "c"))
        list.sortAlphabetically()
        assertEquals(listOf("A", "b", "c"), list.items)
    }

    @Test
    fun serialize_roundTrip() {
        val list = TaskList(listOf("one", "two"))
        assertEquals(list.items, TaskList.deserialize(list.serialize()).items)
        assertEquals(emptyList<String>(), TaskList.deserialize(null).items)
    }

    @Test
    fun shareText_isNumbered() {
        assertEquals("1. a\n2. b", TaskList(listOf("a", "b")).toShareText())
    }
}
