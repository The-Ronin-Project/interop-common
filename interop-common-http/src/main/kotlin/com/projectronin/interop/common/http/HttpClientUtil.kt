package com.projectronin.interop.common.http

import com.projectronin.interop.common.http.exceptions.RequestFailureException
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

/**
 * Wrapper around any call to HttpClient to ensure responses are properly handled, whether that be a successful request,
 * an exception received from the server or an exception that occurs within the client.
 */
inline fun HttpClient.request(
    serverName: String,
    url: String,
    block: HttpClient.(url: String) -> HttpResponse,
): HttpResponse =
    runCatching { block.invoke(this, url) }.fold(
        onSuccess = {
            it.throwExceptionFromHttpStatus(serverName, url)
            it
        },
        onFailure = {
            throw RequestFailureException(it, serverName, url)
        },
    )
