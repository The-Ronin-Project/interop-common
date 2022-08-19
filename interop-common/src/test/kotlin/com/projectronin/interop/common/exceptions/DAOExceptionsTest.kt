package com.projectronin.interop.common.exceptions

import com.projectronin.interop.common.logmarkers.LogMarkers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DAOExceptionsTest {

    @Test
    fun `insert test`() {
        var exception = DatabaseInsertFailureException("EHR")
        assertEquals(LogMarkers.DATABASE_FAILURE, exception.logMarker)
        exception = DatabaseInsertFailureException("EHR", "Invalid key")
        assertEquals(exception.message, "Database INSERT failed for EHR. Error text: Invalid key")
    }

    @Test
    fun `read test`() {
        var exception = DatabaseReadFailureException("EHR")
        assertEquals(LogMarkers.DATABASE_FAILURE, exception.logMarker)
        exception = DatabaseReadFailureException("EHR", "Invalid key")
        assertEquals(exception.message, "Database READ failed for EHR. Error text: Invalid key")
    }

    @Test
    fun `delete test`() {
        var exception = DatabaseDeleteFailureException("EHR")
        assertEquals(LogMarkers.DATABASE_FAILURE, exception.logMarker)
        exception = DatabaseDeleteFailureException("EHR", "Invalid key")
        assertEquals(exception.message, "Database DELETE failed for EHR. Error text: Invalid key")
    }

    @Test
    fun `update test`() {
        var exception = DatabaseUpdateFailureException("EHR")
        assertEquals(LogMarkers.DATABASE_FAILURE, exception.logMarker)
        exception = DatabaseUpdateFailureException("EHR", "Invalid key")
        assertEquals(exception.message, "Database UPDATE failed for EHR. Error text: Invalid key")
    }
}
