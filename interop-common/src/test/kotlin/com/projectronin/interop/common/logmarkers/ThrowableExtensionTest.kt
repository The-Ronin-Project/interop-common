package com.projectronin.interop.common.logmarkers

import com.projectronin.interop.common.exceptions.LogMarkingException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ThrowableExtensionTest {
    @Test
    fun `logMarker works`() {
        val exception = TestException()
        val marker = exception.getLogMarker()
        assertEquals(LogMarkers.TENANT_SERVER_FAILURE, marker)
    }
    @Test
    fun `unknown exception returns null`() {
        val exception = Exception("exception")
        val marker = exception.getLogMarker()
        assertNull(marker)
    }
    private class TestException : LogMarkingException("Always bad!") {
        override val logMarker = LogMarkers.TENANT_SERVER_FAILURE
    }
}
