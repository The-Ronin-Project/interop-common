package com.projectronin.interop.common.http.spring

import com.projectronin.interop.common.http.NO_RETRY_HEADER
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HttpSpringConfigTest {
    @Test
    fun `ensure config can be created and http client can be retrieved`() {
        val httpClient = HttpSpringConfig().getHttpClient()
        assertNotNull(httpClient)
    }

    @Test
    fun `http client supports retry`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        val successfulResponse = """{"status":"active"}"""
        mockWebServer.enqueue(
            MockResponse().setBody(successfulResponse).setHeader("Content-Type", "application/json")
        )
        mockWebServer.start()

        val response = runBlocking {
            HttpSpringConfig().getHttpClient().get(mockWebServer.url("/test").toString()) {
                accept(ContentType.Application.Json)
            }.bodyAsText()
        }
        assertEquals(successfulResponse, response)

        assertEquals(2, mockWebServer.requestCount)

        mockWebServer.shutdown()
    }

    @Test
    fun `http client only retries 3 times`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.InternalServerError.value))
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.NotImplemented.value))
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.BadGateway.value))
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.ServiceUnavailable.value))

        // This will never be called and is simply here to validate the failure
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.OK.value))

        mockWebServer.start()

        val response =
            runBlocking {
                HttpSpringConfig().getHttpClient().get(mockWebServer.url("/test").toString()) {
                    accept(ContentType.Application.Json)
                }
            }
        assertEquals(HttpStatusCode.ServiceUnavailable, response.status)

        assertEquals(4, mockWebServer.requestCount)

        mockWebServer.shutdown()
    }

    @Test
    fun `http client ignores retry on socket timeout if no retry header is set`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        mockWebServer.start()

        assertThrows<SocketTimeoutException> {
            runBlocking {
                HttpSpringConfig().getHttpClient().get(mockWebServer.url("/test").toString()) {
                    headers {
                        append(NO_RETRY_HEADER, "true")
                    }
                    accept(ContentType.Application.Json)
                }
            }
        }

        assertEquals(1, mockWebServer.requestCount)

        mockWebServer.shutdown()
    }

    @Test
    fun `http client ignores retry on 500 if no retry header is set`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        mockWebServer.start()

        val response = runBlocking {
            HttpSpringConfig().getHttpClient().get(mockWebServer.url("/test").toString()) {
                headers {
                    append(NO_RETRY_HEADER, "true")
                }
                accept(ContentType.Application.Json)
            }
        }
        assertEquals(HttpStatusCode.InternalServerError, response.status)

        assertEquals(1, mockWebServer.requestCount)

        mockWebServer.shutdown()
    }
}
