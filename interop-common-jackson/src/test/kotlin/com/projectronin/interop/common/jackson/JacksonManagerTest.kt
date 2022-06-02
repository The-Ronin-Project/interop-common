package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalTime

class JacksonManagerTest {
    @Test
    fun `returns JSON mapper`() {
        val objectMapper = JacksonManager.objectMapper
        assertInstanceOf(JsonMapper::class.java, objectMapper)
    }

    @Test
    fun `adds kotlin module`() {
        val objectMapper = JacksonManager.objectMapper
        assertTrue(objectMapper.registeredModuleIds.contains(KotlinModule.Builder().build().typeId))
    }

    @Test
    fun `adds java time module`() {
        val objectMapper = JacksonManager.objectMapper
        assertTrue(objectMapper.registeredModuleIds.contains(JavaTimeModule().typeId))
    }

    @Test
    fun `serializes and deserializes LocalTime as expected`() {
        val localTime = LocalTime.of(11, 30)
        val serialized = JacksonManager.objectMapper.writeValueAsString(localTime)

        val expected = "\"11:30:00\""
        assertEquals(expected, serialized)

        val deserialized = JacksonManager.objectMapper.readValue<LocalTime>(serialized)
        assertEquals(localTime, deserialized)
    }

    @Test
    fun `serializes and deserializes Instant as expected`() {
        val instant = Instant.ofEpochMilli(1649698716123)
        val serialized = JacksonManager.objectMapper.writeValueAsString(instant)

        val expected = "\"2022-04-11T17:38:36.123Z\""
        assertEquals(expected, serialized)

        val deserialized = JacksonManager.objectMapper.readValue<Instant>(expected)
        assertEquals(instant, deserialized)
    }

    @Test
    fun `disables fail on unknown properties`() {
        val objectMapper = JacksonManager.objectMapper
        assertFalse(objectMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES))
    }

    @Test
    fun `serializes only non-empty fields`() {
        val objectMapper = JacksonManager.objectMapper
        assertEquals(
            JsonInclude.Include.NON_EMPTY,
            objectMapper.serializationConfig.defaultPropertyInclusion.valueInclusion
        )
    }

    @Test
    fun `alternate object mapper serializes non-empty fields`() {
        val objectMapper = JacksonManager.nonAbsentObjectMapper
        assertEquals(
            JsonInclude.Include.NON_ABSENT,
            objectMapper.serializationConfig.defaultPropertyInclusion.valueInclusion
        )
    }
}
