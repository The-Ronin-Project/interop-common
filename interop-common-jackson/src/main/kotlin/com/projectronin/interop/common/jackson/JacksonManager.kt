package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

/**
 * Manager for Jackson configuration.
 */
class JacksonManager {
    companion object {
        /**
         * The JSON object mapper for Jackson.
         */
        val objectMapper = setUpMapper(jsonMapper())

        /**
         * Alternate JSON object mapper for Jackson which will serialize empty fields
         */
        val nonAbsentObjectMapper = setUpMapper(jsonMapper(), JsonInclude.Include.NON_ABSENT)

        /**
         * Sets up the supplied [objectMapper] with the required details for using Jackson within Interops.
         */
        fun <T : ObjectMapper> setUpMapper(
            objectMapper: T,
            jsonInclude: JsonInclude.Include = JsonInclude.Include.NON_EMPTY
        ): T {
            with(objectMapper) {
                registerKotlinModule()
                registerModule(JavaTimeModule())
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                setSerializationInclusion(jsonInclude)
            }
            return objectMapper
        }
    }
}
