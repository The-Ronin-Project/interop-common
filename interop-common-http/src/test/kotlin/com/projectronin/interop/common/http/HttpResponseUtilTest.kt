package com.projectronin.interop.common.http

import com.projectronin.interop.common.http.exceptions.ClientAuthenticationException
import com.projectronin.interop.common.http.exceptions.ClientFailureException
import com.projectronin.interop.common.http.exceptions.ServerFailureException
import com.projectronin.interop.common.http.exceptions.ServiceUnavailableException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HttpResponseUtilTest {
    private val response = mockk<HttpResponse> {
        every { request.url.host } returns "googlay.com"
    }

    @Test
    fun `unauthorized works`() {
        every { response.status } returns HttpStatusCode.Unauthorized
        assertThrows<ClientAuthenticationException> {
            response.throwExceptionFromHttpStatus("Fake Aidbox")
        }
    }

    @Test
    fun `generic client error works`() {
        every { response.status } returns HttpStatusCode.UnsupportedMediaType
        assertThrows<ClientFailureException> {
            response.throwExceptionFromHttpStatus("MDA's AppOrchard", "/url/r4")
        }
    }

    @Test
    fun `service unavailable works`() {
        every { response.status } returns HttpStatusCode.ServiceUnavailable
        assertThrows<ServiceUnavailableException> {
            response.throwExceptionFromHttpStatus("Mystery Server", "some API")
        }
    }

    @Test
    fun `generic server error works`() {
        every { response.status } returns HttpStatusCode.InternalServerError
        assertThrows<ServerFailureException> {
            response.throwExceptionFromHttpStatus("Netflix.com")
        }
    }

    @Test
    fun `good exception does not throw error`() {
        every { response.status } returns HttpStatusCode.OK

        response.throwExceptionFromHttpStatus("Netflix.com")
        assertTrue(true)
    }
}
