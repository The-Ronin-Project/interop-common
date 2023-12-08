package com.projectronin.interop.common.http

import com.projectronin.interop.common.auth.Authentication
import com.projectronin.interop.common.auth.BrokeredAuthenticator
import com.projectronin.interop.common.http.exceptions.ClientAuthenticationException
import com.projectronin.interop.common.http.exceptions.RequestFailureException
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import mu.KLogger

/**
 * Wrapper around any call to HttpClient to ensure responses are properly handled, whether that be a successful request,
 * an exception received from the server or an exception that occurs within the client.
 */
inline fun HttpClient.request(
    serverName: String,
    url: String,
    block: HttpClient.(url: String) -> HttpResponse,
): HttpResponse {
    return this.requestHelper(serverName, url, null, 0, null) { url, _ ->
        block.invoke(this, url)
    }
}

/**
 * Overloaded method to request to allow for authentication retries when a 401 is received
 * A BrokeredAuthenticator must be passed in for retries on Auth
 */
inline fun HttpClient.request(
    serverName: String,
    url: String,
    authenticator: BrokeredAuthenticator,
    numAuthRetries: Int = 1,
    logger: KLogger,
    block: HttpClient.(url: String, auth: Authentication?) -> HttpResponse,
): HttpResponse {
    return this.requestHelper(serverName, url, authenticator, numAuthRetries, logger, block)
}

inline fun HttpClient.requestHelper(
    serverName: String,
    url: String,
    authenticator: BrokeredAuthenticator? = null,
    numAuthRetries: Int = 1,
    logger: KLogger? = null,
    block: HttpClient.(url: String, auth: Authentication?) -> HttpResponse,
): HttpResponse {
    val numTries = numAuthRetries + 1
    var result: HttpResponse? = null
    var auth = authenticator?.getAuthentication()
    for (attempt in 1..numTries) {
        try {
            result =
                runCatching {
                    block.invoke(this, url, auth)
                }.fold(
                    onSuccess = {
                        it.throwExceptionFromHttpStatus(serverName, url)
                        it
                    },
                    onFailure = {
                        throw RequestFailureException(it, serverName, url)
                    },
                )
            break
        } catch (e: ClientAuthenticationException) {
            if (e.status.value != HttpStatusCode.Unauthorized.value) {
                throw e
            }
            if (authenticator == null) {
                logger?.warn { "Received a 401 and no authenticator was found, not retrying for auth" }
                throw e
            }
            if (attempt < numTries) {
                logger?.warn { "Received a 401, reauthenticating and retrying with ${numTries - attempt} tries remaining" }
                auth = authenticator.getAuthentication(true)
            } else {
                logger?.error { "Received a 401, exhausted reauthentication retires" }
                throw e
            }
        }
    }
    return result!!
}
