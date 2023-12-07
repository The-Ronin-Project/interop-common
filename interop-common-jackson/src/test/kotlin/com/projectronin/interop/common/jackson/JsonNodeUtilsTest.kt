package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JsonNodeUtilsTest {
    @Test
    fun `getAsTextOrNull returns null for unknown field`() {
        val node = jsonParser("""{"field":"banana"}""").readValueAsTree<JsonNode>()
        assertNull(node.getAsTextOrNull("field2"))
    }

    @Test
    fun `getAsTextOrNull returns text for field`() {
        val node = jsonParser("""{"field":"banana"}""").readValueAsTree<JsonNode>()
        assertEquals("banana", node.getAsTextOrNull("field"))
    }

    @Test
    fun `getAsIntOrNull returns null for unknown field`() {
        val node = jsonParser("""{"field":12}""").readValueAsTree<JsonNode>()
        assertNull(node.getAsIntOrNull("field2"))
    }

    @Test
    fun `getAsIntOrNull returns int for field`() {
        val node = jsonParser("""{"field":12}""").readValueAsTree<JsonNode>()
        assertEquals(12, node.getAsIntOrNull("field"))
    }

    @Test
    fun `getAsDoubleOrNull returns null for unknown field`() {
        val node = jsonParser("""{"field":3.14}""").readValueAsTree<JsonNode>()
        assertNull(node.getAsDoubleOrNull("field2"))
    }

    @Test
    fun `getAsDoubleOrNull returns double for field`() {
        val node = jsonParser("""{"field":3.14}""").readValueAsTree<JsonNode>()
        assertEquals(3.14, node.getAsDoubleOrNull("field"))
    }

    @Test
    fun `getAs throws exception for unknown field`() {
        data class CustomType(
            val id: Int,
            val name: String,
        )

        val parser = jsonParser("""{"field":{"id":1,"name":"custom"}}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertThrows<NullPointerException> { node.getAs<CustomType>("field2", parser) }
    }

    @Test
    fun `getAs returns requested type for field`() {
        data class CustomType(
            val id: Int,
            val name: String,
        )

        val parser = jsonParser("""{"field":{"id":1,"name":"custom"}}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertEquals(CustomType(1, "custom"), node.getAs<CustomType>("field", parser))
    }

    @Test
    fun `getAsOrNull returns null for unknown field`() {
        data class CustomType(
            val id: Int,
            val name: String,
        )

        val parser = jsonParser("""{"field":{"id":1,"name":"custom"}}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertNull(node.getAsOrNull<CustomType>("field2", parser))
    }

    @Test
    fun `getAsOrNull returns requested type for field`() {
        data class CustomType(
            val id: Int,
            val name: String,
        )

        val parser = jsonParser("""{"field":{"id":1,"name":"custom"}}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertEquals(CustomType(1, "custom"), node.getAsOrNull<CustomType>("field", parser))
    }

    @Test
    fun `getAsList returns empty list for unknown field`() {
        val parser = jsonParser("""{"field":["a","b","c"]}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertEquals(listOf<String>(), node.getAsList<String>("field2", parser))
    }

    @Test
    fun `getAsList returns list of requested types for field`() {
        val parser = jsonParser("""{"field":["a","b","c"]}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertEquals(listOf("a", "b", "c"), node.getAsList<String>("field", parser))
    }

    @Test
    fun `readValueAs reads the current parser as the requested type`() {
        data class CustomType(
            val id: Int,
            val name: String,
        )

        val parser = jsonParser("""{"id":1,"name":"custom"}""")
        val node = parser.readValueAsTree<JsonNode>()
        assertEquals(CustomType(1, "custom"), node.readValueAs(parser, CustomType::class.java))
    }

    @Test
    fun `fieldsStartingWith returns empty List when no fields match`() {
        val node = jsonParser("""{"field1":3.14,"field2":25}""").readValueAsTree<JsonNode>()
        assertEquals(listOf<String>(), node.fieldsStartingWith("ghi"))
    }

    @Test
    fun `fieldsStartingWith returns populated List when fields match`() {
        val node = jsonParser("""{"field1":3.14,"field2":25}""").readValueAsTree<JsonNode>()
        assertEquals(listOf("field1", "field2"), node.fieldsStartingWith("fie"))
    }

    private fun jsonParser(json: String): JsonParser = JacksonManager.objectMapper.createParser(json)
}
