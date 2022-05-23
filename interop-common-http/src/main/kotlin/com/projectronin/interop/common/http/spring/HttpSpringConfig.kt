package com.projectronin.interop.common.http.spring

import com.projectronin.interop.common.http.ktor.ContentLengthSupplier
import com.projectronin.interop.common.jackson.JacksonManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson {
                    JacksonManager.setUpMapper(this)
                }
            }
            install(ContentLengthSupplier)
            install(Logging) {
                level = LogLevel.ALL
            }
            expectSuccess = true
        }
    }
}
