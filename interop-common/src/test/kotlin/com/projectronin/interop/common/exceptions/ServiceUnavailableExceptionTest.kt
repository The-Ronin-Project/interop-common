package com.projectronin.interop.common.exceptions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ServiceUnavailableExceptionTest {
    @Test
    fun `creates exception message correctly`() {
        val serviceUnavailableException = ServiceUnavailableException("service", "message")
        assertEquals("service: message", serviceUnavailableException.message)
    }
}
