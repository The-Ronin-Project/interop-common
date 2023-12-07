package com.projectronin.interop.common.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ListUtilTest {
    @Test
    fun `associateWithNonNull with transform always returning null`() {
        val original = listOf(1, 2, 3, 4, 5)
        val mapped = original.associateWithNonNull { null }
        assertEquals(0, mapped.size)
    }

    @Test
    fun `associateWithNonNull with transform sometimes returning null`() {
        val original = listOf(1, 2, 3, 4, 5)
        val mapped =
            original.associateWithNonNull {
                if (it % 2 == 1) null else "even"
            }

        assertEquals(2, mapped.size)
        assertEquals(mapOf(2 to "even", 4 to "even"), mapped)
    }

    @Test
    fun `associateWithNonNull with transform never returning null`() {
        val original = listOf(1, 2, 3, 4, 5)
        val mapped =
            original.associateWithNonNull {
                it.toString()
            }

        assertEquals(5, mapped.size)
        assertEquals(
            mapOf(
                1 to 1.toString(),
                2 to 2.toString(),
                3 to 3.toString(),
                4 to 4.toString(),
                5 to 5.toString(),
            ),
            mapped,
        )
    }

    @Test
    fun `associateWithNonNull with empty list`() {
        val original = emptyList<Any>()
        val mapped =
            original.associateWithNonNull {
                it.hashCode()
            }

        assertEquals(0, mapped.size)
    }

    @Test
    fun `associateWithNonNull with empty transformer returns Unit values`() {
        val original = listOf("1", "2", "3", "4", "5")
        val mapped = original.associateWithNonNull { }
        assertEquals(5, mapped.size)
        assertEquals(
            mapOf(
                "1" to Unit,
                "2" to Unit,
                "3" to Unit,
                "4" to Unit,
                "5" to Unit,
            ),
            mapped,
        )
    }

    @Test
    fun `associateWithNonNull with non-unique elements in list`() {
        val original = listOf("1", "2", "3", "1")
        var iteration = 0
        val mapped =
            original.associateWithNonNull {
                ++iteration
            }
        assertEquals(
            mapOf(
                "1" to 4,
                "2" to 2,
                "3" to 3,
            ),
            mapped,
        )
    }
}
