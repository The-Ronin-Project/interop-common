package com.projectronin.interop.common.resource

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ResourceTypeTest {
    @Test
    fun `all resource types available`() {
        val allValues = ResourceType.values()
        assertEquals(19, allValues.size)
    }
}
