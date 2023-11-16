package com.projectronin.interop.common.http.spring

import com.projectronin.interop.common.http.ktor.ContentLengthSupplier
import com.projectronin.interop.common.http.retry
import com.projectronin.interop.common.jackson.JacksonManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.serialization.jackson.jackson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.concurrent.CancellationException

@Configuration
class HttpSpringConfig {
    @Bean
    @Primary
    fun getHttpClient(): HttpClient {
        // Now that we're handling exceptions directly, we no longer want to set the expectSuccess flag.
        // Doing so causes Ktor to throw a general exception before we can read the Http response status and
        // decide what to do with it.  See https://ktor.io/docs/response-validation.html
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                jackson {
                    JacksonManager.setUpMapper(this)
                }
            }
            install(ContentLengthSupplier)
            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000 // this is ktor's default
            }
            install(HttpRequestRetry) {
                val maxRetries = 3

                // The following are effectively the logic built into the retry, but with our own initial
                // check for whether the request should retry or not.
                retryOnExceptionIf { httpRequestBuilder, cause ->
                    if (httpRequestBuilder.retry()) {
                        when (cause.unwrapCancellationException()) {
                            // If the underlying exception is a timeout, then retry
                            is HttpRequestTimeoutException,
                            is ConnectTimeoutException,
                            is SocketTimeoutException -> true

                            // Else, retry if the cause was not a cancellation.
                            else -> cause !is CancellationException
                        }
                    } else {
                        false
                    }
                }
                retryIf(maxRetries) { httpRequest, httpResponse ->
                    httpRequest.retry() && httpResponse.status.value.let { it in 500..599 }
                }

                // Retries after a 1-2s delay
                constantDelay(millis = 1000, randomizationMs = 1000)
            }
        }
    }
}
