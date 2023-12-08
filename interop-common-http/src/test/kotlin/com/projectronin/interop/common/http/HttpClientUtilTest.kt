package com.projectronin.interop.common.http

import com.projectronin.interop.common.auth.Authentication
import com.projectronin.interop.common.auth.BrokeredAuthenticator
import com.projectronin.interop.common.http.exceptions.ClientAuthenticationException
import com.projectronin.interop.common.http.exceptions.ClientFailureException
import com.projectronin.interop.common.http.exceptions.RequestFailureException
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.time.temporal.ChronoUnit

class HttpClientUtilTest {
    // Use our own to prevent having retries included by the real one.
    private val httpClient = HttpClient(OkHttp)
    private lateinit var mockWebServer: MockWebServer
    private val logger = KotlinLogging.logger {}

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

        val requestResponse =
            runBlocking {
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

        val exception =
            assertThrows<ClientFailureException> {
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

        val exception =
            assertThrows<RequestFailureException> {
                runBlocking {
                    httpClient.request("Test", url) { url ->
                        post(url)
                    }
                }
            }
        assertTrue(exception.message!!.startsWith("Received exception when calling Test ($url): "))
    }

    @Test
    fun `authenticated request handles successful response`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.OK.value))
        val url = mockWebServer.url("/test").toString()
        val authenticator = TestAuthenticator()

        val requestResponse =
            runBlocking {
                httpClient.request("Test", url, authenticator, 1, logger) { url, _ ->
                    post(url)
                }
            }
        // 1 call to get initial auth
        assertEquals(1, authenticator.numTimesReloadWasCalled)
        assertEquals(0, authenticator.numTimesCleanupWasCalled)
        assertEquals(HttpStatusCode.OK, requestResponse.status)
    }

    @Test
    fun `authenticated request handles successful response with exception status`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpStatusCode.NotFound.value))
        val url = mockWebServer.url("/test").toString()
        val authenticator = TestAuthenticator()

        val exception =
            assertThrows<ClientFailureException> {
                runBlocking {
                    httpClient.request("Test", url, authenticator, 1, logger) { url, _ ->
                        post(url)
                    }
                }
            }
        // 1 call to get initial auth
        assertEquals(1, authenticator.numTimesReloadWasCalled)
        assertEquals(0, authenticator.numTimesCleanupWasCalled)
        assertEquals("Received 404 Client Error when calling Test [localhost] for $url", exception.message)
    }

    @Test
    fun `authenticated request handles exception on request`() {
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
        val url = mockWebServer.url("/test").toString()
        val authenticator = TestAuthenticator()

        val exception =
            assertThrows<RequestFailureException> {
                runBlocking {
                    httpClient.request("Test", url, authenticator, 1, logger) { url, _ ->
                        post(url)
                    }
                }
            }
        // 1 call to get initial auth
        assertEquals(1, authenticator.numTimesReloadWasCalled)
        assertEquals(0, authenticator.numTimesCleanupWasCalled)
        assertTrue(exception.message!!.startsWith("Received exception when calling Test ($url): "))
    }

    @Test
    fun `authenticated request retries on 401 until success`() {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatusCode.Unauthorized.value),
        )
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatusCode.OK.value),
        )
        val url = mockWebServer.url("/test").toString()
        val authenticator = TestAuthenticator()

        val requestResponse =
            runBlocking {
                httpClient.request("Test", url, authenticator, 3, logger) { url, _ ->
                    post(url)
                }
            }
        // 1 call to get initial auth
        assertEquals(2, authenticator.numTimesReloadWasCalled)
        assertEquals(1, authenticator.numTimesCleanupWasCalled)
        assertEquals(2, mockWebServer.requestCount)
        assertEquals(200, requestResponse.status.value)
    }

    @Test
    fun `authenticated request goes to max retry on 401`() {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatusCode.Unauthorized.value),
        )
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatusCode.Unauthorized.value),
        )
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatusCode.Unauthorized.value),
        )
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatusCode.Unauthorized.value),
        )
        val url = mockWebServer.url("/test").toString()
        val authenticator = TestAuthenticator()

        val exception =
            assertThrows<ClientAuthenticationException> {
                runBlocking {
                    httpClient.request("Test", url, authenticator, 3, logger) { url, _ ->
                        post(url)
                    }
                }
            }
        // 1 call to get initial auth
        assertEquals(4, authenticator.numTimesReloadWasCalled)
        assertEquals(3, authenticator.numTimesCleanupWasCalled)
        assertEquals(4, mockWebServer.requestCount)
        assertEquals(401, exception.status.value)
    }

    private class TestAuthenticator(private val returnGoodAuthAfterNTries: Int = 0) : BrokeredAuthenticator() {
        private val auth: Authentication =
            mockk {
                every { tokenType } returns "Bearer"
                every { accessToken } returns "Auth-String"
                every { expiresAt } returns Instant.now().plus(1, ChronoUnit.DAYS)
            }

        var numTimesCleanupWasCalled: Int = 0
            private set
        var numTimesReloadWasCalled: Int = 0
            private set

        override fun reloadAuthentication(): Authentication {
            numTimesReloadWasCalled++
            return auth
        }

        override fun cleanupOldAuthentication(
            oldAuthentication: Authentication?,
            newAuthentication: Authentication,
        ) {
            if (oldAuthentication != null) {
                numTimesCleanupWasCalled++
            }
        }
    }
}
