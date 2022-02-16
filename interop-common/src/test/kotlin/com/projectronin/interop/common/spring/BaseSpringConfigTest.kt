package com.projectronin.interop.common.spring

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BaseSpringConfigTest {
    @Test
    fun `ensure config can be created and http client can be retrieved`() {
        val config = BaseSpringConfig()
        val httpClient = config.getHttpClient()
        Assertions.assertNotNull(httpClient)
    }
}
