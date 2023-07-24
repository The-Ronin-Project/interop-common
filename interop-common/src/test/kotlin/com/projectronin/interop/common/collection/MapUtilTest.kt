package com.projectronin.interop.common.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MapUtilTest {
    @Test
    fun `mapValuesNotNull with transform always returning null`() {
        val original = mapOf("1" to 1, "2" to 2)
        val mapped = original.mapValuesNotNull { null }
        assertEquals(0, mapped.size)
    }

    @Test
    fun `mapValuesNotNull with transform sometimes returning null`() {
        val original = mapOf("1" to 1, "2" to 2)
        val mapped = original.mapValuesNotNull { (k, _) -> if (k == "1") null else "2" }
        assertEquals(1, mapped.size)
        assertEquals("2", mapped["2"])
    }

    @Test
    fun `mapValuesNotNull with transform never returning null`() {
        val original = mapOf("1" to 1, "2" to 2)
        val mapped = original.mapValuesNotNull { (_, v) -> v.toString() }
        assertEquals(2, mapped.size)
        assertEquals("1", mapped["1"])
        assertEquals("2", mapped["2"])
    }

    @Test
    fun `mapListValues creates List from single values`() {
        val original = mapOf("1" to 1, "2" to 2)
        val mapped = original.mapListValues()
        assertEquals(2, mapped.size)
        assertEquals(listOf(1), mapped["1"])
        assertEquals(listOf(2), mapped["2"])
    }
}
