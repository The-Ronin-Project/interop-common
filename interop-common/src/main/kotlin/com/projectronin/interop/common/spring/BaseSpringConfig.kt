package com.projectronin.interop.common.spring

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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
class BaseSpringConfig {
    @Bean
    @Primary
    fun getHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson {
                    registerModule(JavaTimeModule())
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
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
