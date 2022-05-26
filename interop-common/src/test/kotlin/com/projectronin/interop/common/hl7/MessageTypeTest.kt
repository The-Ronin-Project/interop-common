package com.projectronin.interop.common.hl7

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MessageTypeTest {
    @Test
    fun `all types types available`() {
        val allValues = MessageType.values()
        Assertions.assertEquals(1, allValues.size)
    }
}
