package com.projectronin.interop.common.jackson

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class JacksonUtilTest {
    @Test
    fun `multiple types work`() {
        val example = ExampleObject("first")
        val example2 = ExampleSecondObject("second")
        val exampleJson = JacksonUtil.writeJsonValue(example)
        val example2Json = JacksonUtil.writeJsonValue(example2)
        val newExample = JacksonUtil.readJsonObject(exampleJson, ExampleObject::class)
        val newExample2 = JacksonUtil.readJsonObject(example2Json, ExampleSecondObject::class)
        assertEquals(example, newExample)
        assertEquals(example2, newExample2)
    }

    @Test
    fun `multiple lists work`() {
        val example = listOf(ExampleObject("first"))
        val example2 = listOf(ExampleSecondObject("second"))
        val exampleJson = JacksonUtil.writeJsonValue(example)
        val example2Json = JacksonUtil.writeJsonValue(example2)
        val newExample = JacksonUtil.readJsonList(exampleJson, ExampleObject::class)
        val newExample2 = JacksonUtil.readJsonList(example2Json, ExampleSecondObject::class)
        assertEquals(example, newExample)
        assertEquals(example2, newExample2)
    }
}

private data class ExampleObject(
    val first: String,
)

private data class ExampleSecondObject(
    val second: String,
)
