package com.projectronin.interop.common.hl7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProcessingIDTest {
    @Test
    fun `all types available`() {
        val allValues = ProcessingID.values()
        assertEquals(5, allValues.size)
    }
}
