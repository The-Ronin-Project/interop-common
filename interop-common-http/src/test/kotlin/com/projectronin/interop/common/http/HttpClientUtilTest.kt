package com.projectronin.interop.common.http

import com.projectronin.interop.common.http.exceptions.ClientFailureException
import com.projectronin.interop.common.http.exceptions.RequestFailureException
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HttpClientUtilTest {
    // Use our own to prevent having retries included by the real one.
    private val httpClient = HttpClient(OkHttp)
    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `handles successful response`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.OK.value))
        val url = mockWebServer.url("/test").toString()

        val requestResponse = runBlocking {
            httpClient.request("Test", url) { url ->
                post(url)
            }
        }
        assertEquals(HttpStatusCode.OK, requestResponse.status)
    }

    @Test
    fun `handles successful response with exception status`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.NotFound.value))
        val url = mockWebServer.url("/test").toString()

        val exception = assertThrows<ClientFailureException> {
            runBlocking {
                httpClient.request("Test", url) { url ->
                    post(url)
                }
            }
        }
        assertEquals("Received 404 Client Error when calling Test [localhost] for $url", exception.message)
    }

    @Test
    fun `handles exception on request`() {
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
        val url = mockWebServer.url("/test").toString()

        val exception = assertThrows<RequestFailureException> {
            runBlocking {
                httpClient.request("Test", url) { url ->
                    post(url)
                }
            }
        }
        assertTrue(exception.message!!.startsWith("Received exception when calling Test ($url): "))
    }
}
