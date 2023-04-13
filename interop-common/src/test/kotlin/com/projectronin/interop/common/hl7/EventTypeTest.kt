package com.projectronin.interop.common.hl7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EventTypeTest {
    @Test
    fun `all event types available`() {
        val allValues = EventType.values()
        assertEquals(3, allValues.size)
    }
}
