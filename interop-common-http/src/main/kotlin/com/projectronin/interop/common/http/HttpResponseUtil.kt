package com.projectronin.interop.common.http.ktor

import com.projectronin.interop.common.http.exceptions.ClientAuthenticationException
import com.projectronin.interop.common.http.exceptions.ClientFailureException
import com.projectronin.interop.common.http.exceptions.ServerFailureException
import com.projectronin.interop.common.http.exceptions.ServiceUnavailableException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request

fun HttpResponse.throwExceptionFromHttpStatus(serverName: String, serviceName: String? = null) {
    val host = this.request.url.host
    val fullServerName = "$serverName [$host]"
    val status = this.status
    when (status.value) {
        in listOf(401, 403, 407) -> throw ClientAuthenticationException(status, fullServerName, serviceName)
        in 400..499 -> throw ClientFailureException(status, fullServerName, serviceName)
        503 -> throw ServiceUnavailableException(status, fullServerName, serviceName)
        in 500..599 -> throw ServerFailureException(status, fullServerName, serviceName)
    }
}
