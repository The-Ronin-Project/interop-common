package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JacksonManagerTest {
    @Test
    fun `returns JSON mapper`() {
        val objectMapper = JacksonManager.objectMapper
        assertTrue(objectMapper is JsonMapper)
    }

    @Test
    fun `adds kotlin module`() {
        val objectMapper = JacksonManager.objectMapper
        assertTrue(objectMapper.registeredModuleIds.contains(KotlinModule::class.java.name))
    }

    @Test
    fun `adds java time module`() {
        val objectMapper = JacksonManager.objectMapper
        assertTrue(objectMapper.registeredModuleIds.contains(JavaTimeModule::class.java.name))
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
}
