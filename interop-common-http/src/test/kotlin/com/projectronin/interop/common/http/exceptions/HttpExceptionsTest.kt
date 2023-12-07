package com.projectronin.interop.common.http.exceptions

import com.projectronin.interop.common.logmarkers.LogMarkers
import com.projectronin.interop.common.logmarkers.getLogMarker
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HttpExceptionsTest {
    @Test
    fun `serviceUnavailableException creates exception message correctly`() {
        val exception =
            ServiceUnavailableException(
                HttpStatusCode.ServiceUnavailable,
                "Fake Server",
            )
        assertEquals("Received 503 Service Unavailable when calling Fake Server", exception.message)
    }

    @Test
    fun `serviceUnavailableException creates exception message correctly with extra information`() {
        val exception =
            ServiceUnavailableException(
                HttpStatusCode.ServiceUnavailable,
                "Fake Server",
                "API",
            )
        assertEquals("Received 503 Service Unavailable when calling Fake Server for API", exception.message)
    }

    @Test
    fun `ClientAuthenticationException creates exception message correctly`() {
        val exception =
            ClientAuthenticationException(
                HttpStatusCode.Unauthorized,
                "Fake Server",
            )
        assertEquals("Received 401 Unauthorized when calling Fake Server", exception.message)
    }

    @Test
    fun `ClientAuthenticationException creates exception message correctly with extra information`() {
        val exception =
            ClientAuthenticationException(
                HttpStatusCode.Unauthorized,
                "Fake Server",
                "API",
            )
        assertEquals("Received 401 Unauthorized when calling Fake Server for API", exception.message)
    }

    @Test
    fun `ServerFailureException creates exception message correctly`() {
        val exception =
            ServerFailureException(
                HttpStatusCode.InternalServerError,
                "Fake Server",
            )
        assertEquals("Received 500 Internal Server Error when calling Fake Server", exception.message)
    }

    @Test
    fun `ServerFailureException creates exception message correctly with extra information`() {
        val exception =
            ServerFailureException(
                HttpStatusCode.InternalServerError,
                "Fake Server",
                "API",
            )
        assertEquals("Received 500 Internal Server Error when calling Fake Server for API", exception.message)
    }

    @Test
    fun `ClientFailureException creates exception message correctly`() {
        val exception =
            ClientFailureException(
                HttpStatusCode.NotFound,
                "Fake Server",
            )
        assertEquals("Received 404 Not Found when calling Fake Server", exception.message)
    }

    @Test
    fun `ClientFailureException creates exception message correctly with extra information`() {
        val exception =
            ClientFailureException(
                HttpStatusCode.NotFound,
                "Fake Server",
                "API",
            )
        assertEquals("Received 404 Not Found when calling Fake Server for API", exception.message)
    }

    @Test
    fun `service unavailable exception returns correct marker`() {
        val exception =
            ServiceUnavailableException(
                HttpStatusCode.ServiceUnavailable,
                "fake server",
                null,
            )
        assertEquals(LogMarkers.SERVICE_UNAVAILABLE, exception.getLogMarker())
    }

    @Test
    fun `service unavailable exception returns correct status`() {
        val exception =
            ServiceUnavailableException(
                HttpStatusCode.ServiceUnavailable,
                "fake server",
                null,
            )
        assertEquals(HttpStatusCode.ServiceUnavailable, exception.status)
    }

    @Test
    fun `client authorization exception returns correct marker`() {
        val exception =
            ClientAuthenticationException(
                HttpStatusCode.Unauthorized,
                "fake server",
                null,
            )
        assertEquals(LogMarkers.AUTHORIZATION, exception.getLogMarker())
    }

    @Test
    fun `client authorization exception returns correct status`() {
        val exception =
            ClientAuthenticationException(
                HttpStatusCode.Unauthorized,
                "fake server",
                null,
            )
        assertEquals(HttpStatusCode.Unauthorized, exception.status)
    }

    @Test
    fun `client failure exception returns correct marker`() {
        val exception =
            ClientFailureException(
                HttpStatusCode.NotFound,
                "fake server",
                null,
            )
        assertEquals(LogMarkers.CLIENT_FAILURE, exception.getLogMarker())
    }

    @Test
    fun `client failure exception returns correct status`() {
        val exception =
            ClientFailureException(
                HttpStatusCode.NotFound,
                "fake server",
                null,
            )
        assertEquals(HttpStatusCode.NotFound, exception.status)
    }

    @Test
    fun `server failure exception returns correct marker`() {
        val exception =
            ServerFailureException(
                HttpStatusCode.InternalServerError,
                "fake server",
                null,
            )
        assertEquals(LogMarkers.SERVER_FAILURE, exception.getLogMarker())
    }

    @Test
    fun `server failure exception returns correct status`() {
        val exception =
            ServerFailureException(
                HttpStatusCode.InternalServerError,
                "fake server",
                null,
            )
        assertEquals(HttpStatusCode.InternalServerError, exception.status)
    }
}
