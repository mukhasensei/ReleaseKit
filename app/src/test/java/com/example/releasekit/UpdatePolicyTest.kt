package com.example.releasekit

import org.junit.Assert.assertEquals
import org.junit.Test

class UpdatePolicyTest {
    @Test
    fun forcedWhenBelowMinSupported() {
        assertEquals(UpdateAction.IMMEDIATE, UpdatePolicy.decide(10200, 10201, 10201, 0))
    }

    @Test
    fun noneWhenUpToDate() {
        assertEquals(UpdateAction.NONE, UpdatePolicy.decide(10201, 0, null, 0))
        assertEquals(UpdateAction.NONE, UpdatePolicy.decide(10201, 0, 10201, 5))
    }

    @Test
    fun flexibleForNormalPriority() {
        assertEquals(UpdateAction.FLEXIBLE, UpdatePolicy.decide(10100, 0, 10201, 1))
    }

    @Test
    fun immediateForHighPriority() {
        assertEquals(UpdateAction.IMMEDIATE, UpdatePolicy.decide(10100, 0, 10201, 4))
    }
}
