package com.projectronin.interop.common.http

import io.ktor.client.request.DefaultHttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.util.InternalAPI
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(InternalAPI::class)
class RetryUtilTest {
    @Test
    fun `HttpRequest without no retry header`() {
        val data = HttpRequestBuilder().build()
        val request = DefaultHttpRequest(mockk(), data)
        assertTrue(request.retry())
    }

    @Test
    fun `HttpRequest with no retry header set to false`() {
        val builder = HttpRequestBuilder()
        builder.headers {
            append(NO_RETRY_HEADER, "false")
        }
        val data = builder.build()
        val request = DefaultHttpRequest(mockk(), data)
        assertTrue(request.retry())
    }

    @Test
    fun `HttpRequest with no retry header set to true`() {
        val builder = HttpRequestBuilder()
        builder.headers {
            append(NO_RETRY_HEADER, "true")
        }
        val data = builder.build()
        val request = DefaultHttpRequest(mockk(), data)
        assertFalse(request.retry())
    }

    @Test
    fun `HttpRequestBuilder without no retry header`() {
        val builder = HttpRequestBuilder()
        assertTrue(builder.retry())
    }

    @Test
    fun `HttpRequestBuilder with no retry header set to false`() {
        val builder = HttpRequestBuilder()
        builder.headers {
            append(NO_RETRY_HEADER, "false")
        }
        assertTrue(builder.retry())
    }

    @Test
    fun `HttpRequestBuilder with no retry header set to true`() {
        val builder = HttpRequestBuilder()
        builder.headers {
            append(NO_RETRY_HEADER, "true")
        }
        assertFalse(builder.retry())
    }
}
