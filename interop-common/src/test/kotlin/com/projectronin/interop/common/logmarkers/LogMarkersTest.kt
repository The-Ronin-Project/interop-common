package com.projectronin.interop.common.logmarkers

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LogMarkersTest {
    @Test
    fun `markers references are set`() {
        assertTrue(LogMarkers.SERVICE_UNAVAILABLE.contains(LogMarkers.EXCLUDE_FROM_GENERAL_ALERTS))
    }
}
