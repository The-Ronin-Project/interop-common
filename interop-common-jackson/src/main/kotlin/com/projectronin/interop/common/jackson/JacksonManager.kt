package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Manager for Jackson configuration.
 */
class JacksonManager {
    companion object {
        /**
         * The JSON object mapper for Jackson.
         */
        val objectMapper = jsonMapper {
            addModule(kotlinModule())
            addModule(JavaTimeModule().addSerializer(LocalTime::class.java, LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME)))
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            serializationInclusion(JsonInclude.Include.NON_EMPTY)
        }
    }
}
