package com.projectronin.interop.common.http.spring

import com.projectronin.interop.common.http.ktor.ContentLengthSupplier
import com.projectronin.interop.common.jackson.JacksonManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson.jackson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

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
            install(HttpRequestRetry) {
                // This will cause a retry for a 5xx error or an exception received during the request.
                retryOnExceptionOrServerErrors(3)
                // Retries after a 1-2s delay
                constantDelay(millis = 1000, randomizationMs = 1000)
            }
        }
    }
}
