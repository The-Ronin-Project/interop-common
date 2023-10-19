package com.projectronin.interop.common.http.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthenticationConfigTest {

    @Test
    fun `defaults work - but spring would prevent them`() {
        val defaultConfig = AuthenticationConfig()
        assertEquals("", defaultConfig.token.url)
        assertEquals("", defaultConfig.audience)
        assertEquals("", defaultConfig.client.id)
        assertEquals("", defaultConfig.client.secret)
        assertEquals(AuthMethod.AUTH0, defaultConfig.method)
    }

    @Test
    fun `defaults are 100 percent required bozo`() {
        val authenticationSpringConfig = AuthenticationConfig(
            token = Token("auth"),
            audience = "aud",
            client = Client(id = "clientId", secret = "clientSecret"),
            method = AuthMethod.STANDARD
        )
        assertEquals("auth", authenticationSpringConfig.token.url)
        assertEquals("aud", authenticationSpringConfig.audience)
        assertEquals("clientId", authenticationSpringConfig.client.id)
        assertEquals("clientSecret", authenticationSpringConfig.client.secret)
        assertEquals(AuthMethod.STANDARD, authenticationSpringConfig.method)
    }
}
