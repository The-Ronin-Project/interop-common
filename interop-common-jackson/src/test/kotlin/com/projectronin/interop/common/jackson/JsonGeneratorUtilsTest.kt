package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.MappingJsonFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter

class JsonGeneratorUtilsTest {
    @Test
    fun `empty lists are not written`() {
        val output =
            generate {
                it.writeListField("field", listOf<String>())
            }
        assertEquals("{}", output)
    }

    @Test
    fun `non-empty lists are written`() {
        val output =
            generate {
                it.writeListField("field", listOf("a", "b", "c"))
            }
        assertEquals("""{"field":["a","b","c"]}""", output)
    }

    @Test
    fun `null values are not written`() {
        val output =
            generate {
                it.writeNullableField("field", null)
            }
        assertEquals("{}", output)
    }

    @Test
    fun `non-null strings are written`() {
        val output =
            generate {
                it.writeNullableField("field", "value")
            }
        assertEquals("""{"field":"value"}""", output)
    }

    @Test
    fun `non-null doubles are written`() {
        val output =
            generate {
                it.writeNullableField("field", 3.14)
            }
        assertEquals("""{"field":3.14}""", output)
    }

    @Test
    fun `non-null ints are written`() {
        val output =
            generate {
                it.writeNullableField("field", 25)
            }
        assertEquals("""{"field":25}""", output)
    }

    @Test
    fun `non-null booleans are written`() {
        val output =
            generate {
                it.writeNullableField("field", false)
            }
        assertEquals("""{"field":false}""", output)
    }

    @Test
    fun `non-null custom types are written`() {
        data class CustomType(
            val id: Int,
            val name: String,
        )

        val output =
            generate {
                it.writeNullableField("field", CustomType(1, "custom"))
            }
        assertEquals("""{"field":{"id":1,"name":"custom"}}""", output)
    }

    private fun generate(innerObject: (JsonGenerator) -> Unit): String {
        StringWriter().use { writer ->
            val jsonGenerator = MappingJsonFactory().createGenerator(writer)
            jsonGenerator.writeStartObject()
            innerObject(jsonGenerator)
            jsonGenerator.writeEndObject()
            jsonGenerator.flush()
            return writer.toString()
        }
    }
}
