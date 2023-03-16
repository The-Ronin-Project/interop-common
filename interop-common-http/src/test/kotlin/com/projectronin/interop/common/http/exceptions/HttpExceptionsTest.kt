package com.projectronin.interop.common.http.exceptions

import com.projectronin.interop.common.logmarkers.LogMarkers
import com.projectronin.interop.common.logmarkers.getLogMarker
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HttpExceptionsTest {
    private val defaultMessage = "Received 503 Service Unavailable when calling Fake Server"
    private val extendedMessage = "Received 503 Service Unavailable when calling Fake Server for API"

    @Test
    fun `serviceUnavailableException creates exception message correctly`() {
        val exception = ServiceUnavailableException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server"
        )
        assertEquals(defaultMessage, exception.message)
    }

    @Test
    fun `serviceUnavailableException creates exception message correctly with extra information`() {
        val exception = ServiceUnavailableException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server",
            "API"
        )
        assertEquals(extendedMessage, exception.message)
    }

    @Test
    fun `ClientAuthenticationException creates exception message correctly`() {
        val exception = ClientAuthenticationException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server"
        )
        assertEquals(defaultMessage, exception.message)
    }

    @Test
    fun `ClientAuthenticationException creates exception message correctly with extra information`() {
        val exception = ClientAuthenticationException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server",
            "API"
        )
        assertEquals(extendedMessage, exception.message)
    }

    @Test
    fun `ServerFailureException creates exception message correctly`() {
        val exception = ServerFailureException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server"
        )
        assertEquals(defaultMessage, exception.message)
    }

    @Test
    fun `ServerFailureException creates exception message correctly with extra information`() {
        val exception = ServerFailureException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server",
            "API"
        )
        assertEquals(extendedMessage, exception.message)
    }

    @Test
    fun `ClientFailureException creates exception message correctly`() {
        val exception = ClientFailureException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server"
        )
        assertEquals(defaultMessage, exception.message)
    }

    @Test
    fun `ClientFailureException creates exception message correctly with extra information`() {
        val exception = ClientFailureException(
            HttpStatusCode.ServiceUnavailable,
            "Fake Server",
            "API"
        )
        assertEquals(extendedMessage, exception.message)
    }

    @Test
    fun `service unavailable exception returns correct marker`() {
        val exception = ServiceUnavailableException(
            HttpStatusCode.ServiceUnavailable,
            "fake server",
            null
        )
        val marker = exception.getLogMarker()!!
        assertEquals(marker, LogMarkers.SERVICE_UNAVAILABLE)
    }

    @Test
    fun `client authorization exception returns correct marker`() {
        val exception = ClientAuthenticationException(
            HttpStatusCode.ServiceUnavailable,
            "fake server",
            null
        )
        val marker = exception.getLogMarker()!!
        assertEquals(marker, LogMarkers.AUTHORIZATION)
    }

    @Test
    fun `client exception returns correct marker`() {
        val exception = ClientFailureException(
            HttpStatusCode.ServiceUnavailable,
            "fake server",
            null
        )
        val marker = exception.getLogMarker()!!
        assertEquals(marker, LogMarkers.CLIENT_FAILURE)
    }

    @Test
    fun `server exception returns correct marker`() {
        val exception = ServerFailureException(
            HttpStatusCode.ServiceUnavailable,
            "fake server",
            null
        )
        val marker = exception.getLogMarker()!!
        assertEquals(marker, LogMarkers.SERVER_FAILURE)
    }
}
