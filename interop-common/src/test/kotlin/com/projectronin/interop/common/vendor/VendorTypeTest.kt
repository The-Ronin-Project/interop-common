package com.projectronin.interop.common.vendor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VendorTypeTest {
    @Test
    fun `all vendor types available`() {
        val allValues = VendorType.values()
        assertEquals(2, allValues.size)
    }
}
