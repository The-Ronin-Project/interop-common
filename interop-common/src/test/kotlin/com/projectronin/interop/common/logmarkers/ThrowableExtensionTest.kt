package com.projectronin.interop.common.logmarkers

import com.projectronin.interop.common.exceptions.ServiceUnavailableException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ThrowableExtensionTest {
    @Test
    fun `service unavailable exception returns correct marker`() {
        val exception = ServiceUnavailableException("service", "message")
        val marker = exception.getLogMarker()!!
        assertEquals(marker, LogMarkers.SERVICE_UNAVAILABLE)
    }

    @Test
    fun `unknown exception returns null`() {
        val exception = Exception("exception")
        val marker = exception.getLogMarker()
        assertNull(marker)
    }
}
