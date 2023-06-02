package com.projectronin.interop.common.logmarkers

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LogMarkersTest {
    @Test
    fun `markers references are set`() {
        assertTrue(LogMarkers.SERVICE_UNAVAILABLE.contains(LogMarkers.EXCLUDE_FROM_GENERAL_ALERTS))
        assertTrue(LogMarkers.AUTHORIZATION.contains(LogMarkers.EXCLUDE_FROM_GENERAL_ALERTS))
        assertTrue(LogMarkers.VALIDATION_ISSUE.contains(LogMarkers.EXCLUDE_FROM_GENERAL_ALERTS))
        assertTrue(LogMarkers.ILLEGAL_ARGUMENT.contains(LogMarkers.EXCLUDE_FROM_GENERAL_ALERTS))
    }

    @Test
    fun `markers exist`() {
        assertNotNull(LogMarkers.CLIENT_FAILURE)
        assertNotNull(LogMarkers.SERVER_FAILURE)
        assertNotNull(LogMarkers.SERVICE_UNAVAILABLE)
        assertNotNull(LogMarkers.AUTHORIZATION)
        assertNotNull(LogMarkers.CLIENT_FAILURE)
        assertNotNull(LogMarkers.TENANT_SERVER_FAILURE)
        assertNotNull(LogMarkers.HTTP_REQUEST_FAILURE)
        assertNotNull(LogMarkers.VALIDATION_ISSUE)
        assertNotNull(LogMarkers.ILLEGAL_ARGUMENT)
    }
}
