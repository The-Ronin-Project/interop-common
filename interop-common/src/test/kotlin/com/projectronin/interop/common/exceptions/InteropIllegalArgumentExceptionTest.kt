package com.projectronin.interop.common.exceptions

import com.projectronin.interop.common.logmarkers.LogMarkers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InteropIllegalArgumentExceptionTest {
    @Test
    fun `returns supplied message`() {
        val exception = InteropIllegalArgumentException("required")
        assertEquals("required", exception.message)
    }

    @Test
    fun `returns proper LogMarker`() {
        val exception = InteropIllegalArgumentException("required")
        assertEquals(LogMarkers.ILLEGAL_ARGUMENT, exception.logMarker)
    }
}
