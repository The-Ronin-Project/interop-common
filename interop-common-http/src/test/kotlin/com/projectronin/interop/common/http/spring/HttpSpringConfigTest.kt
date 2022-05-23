package com.projectronin.interop.common.http.spring

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class HttpSpringConfigTest {
    @Test
    fun `ensure config can be created and http client can be retrieved`() {
        val httpClient = HttpSpringConfig().getHttpClient()
        assertNotNull(httpClient)
    }
}
