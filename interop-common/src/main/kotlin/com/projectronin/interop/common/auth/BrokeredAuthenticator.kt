package com.projectronin.interop.common.auth

import mu.KotlinLogging
import java.time.Instant

/**
 * Supports authentication workflows that would like to use a brokered cache to prevent repeat Authentication loads.
 */
abstract class BrokeredAuthenticator(
    protected val expirationBufferInSeconds: Long = 60,
) {
    protected val logger = KotlinLogging.logger(this::class.java.name)
    private var cachedAuthentication: Authentication? = null

    abstract fun reloadAuthentication(): Authentication

    /**
     * Retrieves the current [Authentication] to use.
     * If fetchNewAuthentication is true, we will always grab a new auth from [reloadAuthentication]
     */
    fun getAuthentication(fetchNewAuthentication: Boolean = false): Authentication {
        val isCacheValid =
            cachedAuthentication?.expiresAt?.isAfter(Instant.now().plusSeconds(expirationBufferInSeconds)) ?: false
        if (isCacheValid && !fetchNewAuthentication) {
            logger.debug { "Returning cached authentication" }
            return cachedAuthentication!!
        }

        logger.debug { "Requesting fresh authentication" }
        val oldAuthentication = cachedAuthentication
        val authentication = reloadAuthentication()
        authentication.expiresAt?.let {
            logger.debug { "Retrieved authentication has expiration (${authentication.expiresAt}), so it will be cached" }
            cachedAuthentication = authentication
        }
        cleanupOldAuthentication(oldAuthentication, authentication)

        return authentication
    }

    /**
     * This method can be overridden to support invalidating or cleaning up old credentials
     */
    protected open fun cleanupOldAuthentication(
        oldAuthentication: Authentication?,
        newAuthentication: Authentication,
    ) {
        /*
         * For most authenticators, this method does not need to be overridden, but
         * Aidbox needed additional cleanup after the token was invalidated by aidbox
         * see: INT-2355
         */
    }
}
