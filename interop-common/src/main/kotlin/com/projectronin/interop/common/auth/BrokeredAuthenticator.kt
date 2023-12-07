package com.projectronin.interop.common.auth

import mu.KotlinLogging
import java.time.Instant

/**
 * Supports authentication workflows that would like to use a brokered cache to prevent repeat Authentication loads.
 */
abstract class BrokeredAuthenticator(
    private val expirationBufferInSeconds: Long = 60,
) {
    protected val logger = KotlinLogging.logger(this::class.java.name)
    private var cachedAuthentication: Authentication? = null

    abstract fun reloadAuthentication(): Authentication

    /**
     * Retrieves the current [Authentication] to use.
     */
    fun getAuthentication(): Authentication {
        val isCacheValid =
            cachedAuthentication?.expiresAt?.isAfter(Instant.now().plusSeconds(expirationBufferInSeconds)) ?: false
        if (isCacheValid) {
            logger.debug { "Returning cached authentication" }
            return cachedAuthentication!!
        }

        logger.debug { "Requesting fresh authentication" }
        val authentication = reloadAuthentication()
        authentication.expiresAt?.let {
            logger.debug { "Retrieved authentication has expiration (${authentication.expiresAt}), so it will be cached" }
            cachedAuthentication = authentication
        }
        return authentication
    }
}
