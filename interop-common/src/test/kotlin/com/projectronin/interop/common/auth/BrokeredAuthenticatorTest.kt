package com.projectronin.interop.common.auth

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class BrokeredAuthenticatorTest {
    private lateinit var broker: BrokeredExample
    private lateinit var reloadedAuthentication: Authentication

    private val cachedAuthenticationProperty =
        BrokeredAuthenticator::class.memberProperties.first { it.name == "cachedAuthentication" } as KMutableProperty<Authentication?>

    init {
        cachedAuthenticationProperty.isAccessible = true
    }

    @BeforeEach
    fun initTest() {
        reloadedAuthentication = mockk()
        broker = BrokeredExample(reloadedAuthentication)
    }

    @Test
    fun `loads partially specified authentication when not cached`() {
        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns null
        every { reloadedAuthentication.scope } returns null
        every { reloadedAuthentication.refreshToken } returns null

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)
    }

    @Test
    fun `loads fully specified authentication when not cached`() {
        val expiresAt = LocalDateTime.now().plusSeconds(20).toInstant(ZoneOffset.UTC)

        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns expiresAt
        every { reloadedAuthentication.scope } returns "scope"
        every { reloadedAuthentication.refreshToken } returns "refresh"

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertEquals(expiresAt, authentication.expiresAt)
        assertEquals("scope", authentication.scope)
        assertEquals("refresh", authentication.refreshToken)
    }

    @Test
    fun `caches authentication when expiration provided`() {
        val expiresAt = LocalDateTime.now().plusSeconds(20).toInstant(ZoneOffset.UTC)

        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns expiresAt
        every { reloadedAuthentication.scope } returns null
        every { reloadedAuthentication.refreshToken } returns null

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertNotNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)

        assertEquals(authentication, getCachedAuthentication())
    }

    @Test
    fun `does not cache authentication when no expiration provided`() {
        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns null
        every { reloadedAuthentication.scope } returns null
        every { reloadedAuthentication.refreshToken } returns null

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)

        assertNull(getCachedAuthentication())
    }

    @Test
    fun `loads authentication when cached has no expiration`() {
        val cachedAuthentication = mockk<Authentication> {
            every { expiresAt } returns null
        }
        setCachedAuthentication(cachedAuthentication)

        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns null
        every { reloadedAuthentication.scope } returns null
        every { reloadedAuthentication.refreshToken } returns null

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)
    }

    @Test
    fun `loads authentication when cached has expired`() {
        val cachedAuthentication = mockk<Authentication> {
            every { expiresAt } returns Instant.now().minusSeconds(600)
        }
        setCachedAuthentication(cachedAuthentication)

        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns null
        every { reloadedAuthentication.scope } returns null
        every { reloadedAuthentication.refreshToken } returns null

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)
    }

    @Test
    fun `loads authentication when cached expires within buffer`() {
        val cachedAuthentication = mockk<Authentication> {
            every { expiresAt } returns Instant.now().plusSeconds(25)
        }
        setCachedAuthentication(cachedAuthentication)

        every { reloadedAuthentication.tokenType } returns "Bearer"
        every { reloadedAuthentication.accessToken } returns "token"
        every { reloadedAuthentication.expiresAt } returns null
        every { reloadedAuthentication.scope } returns null
        every { reloadedAuthentication.refreshToken } returns null

        val authentication = broker.getAuthentication()
        assertEquals("Bearer", authentication.tokenType)
        assertEquals("token", authentication.accessToken)
        assertNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)
    }

    @Test
    fun `returns cached authentication when still valid`() {
        val cachedAuthentication = mockk<Authentication> {
            every { expiresAt } returns Instant.now().plusSeconds(600)
            every { tokenType } returns "Basic"
            every { accessToken } returns "cached_token"
            every { scope } returns null
            every { refreshToken } returns null
        }
        setCachedAuthentication(cachedAuthentication)

        val authentication = broker.getAuthentication()
        assertEquals("Basic", authentication.tokenType)
        assertEquals("cached_token", authentication.accessToken)
        assertNotNull(authentication.expiresAt)
        assertNull(authentication.scope)
        assertNull(authentication.refreshToken)
    }

    private fun getCachedAuthentication() = cachedAuthenticationProperty.getter.call(broker)
    private fun setCachedAuthentication(authentication: Authentication?) =
        cachedAuthenticationProperty.setter.call(broker, authentication)

    private class BrokeredExample(private val reloadedAuthentication: Authentication) : BrokeredAuthenticator() {
        override fun reloadAuthentication(): Authentication = reloadedAuthentication
    }
}
