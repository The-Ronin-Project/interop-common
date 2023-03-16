package com.projectronin.interop.common.http.exceptions
import com.projectronin.interop.common.exceptions.LogMarkingException
import com.projectronin.interop.common.logmarkers.LogMarkers
import io.ktor.http.HttpStatusCode

abstract class HttpException(
    status: HttpStatusCode,
    serverName: String,
    serviceName: String?
) : LogMarkingException(
    "Received $status when calling $serverName"
        .appendIfNotNull(serviceName) { " for $serviceName" }
)

/**
 * Exception indicating a generic server failure
 */
class ServerFailureException(
    status: HttpStatusCode,
    serverName: String,
    serviceName: String? = null
) : HttpException(status, serverName, serviceName) {
    override val logMarker = LogMarkers.SERVER_FAILURE
}

/**
 * Exception indicating a generic client request failed
 */

class ClientFailureException(
    status: HttpStatusCode,
    serverName: String,
    serviceName: String? = null
) : HttpException(status, serverName, serviceName) {
    override val logMarker = LogMarkers.CLIENT_FAILURE
}

/**
 * Exception indicating the client was unauthorized
 */
class ClientAuthenticationException(
    status: HttpStatusCode,
    serverName: String,
    serviceName: String? = null
) : HttpException(status, serverName, serviceName) {
    override val logMarker = LogMarkers.AUTHORIZATION
}

/**
 * Exception indicating that a service was unavailable.
 */
class ServiceUnavailableException(
    status: HttpStatusCode,
    serverName: String,
    serviceName: String? = null
) : HttpException(status, serverName, serviceName) {
    override val logMarker = LogMarkers.SERVICE_UNAVAILABLE
}

fun String.appendIfNotNull(nullable: String?, baseString: (String) -> String) =
    if (nullable != null) this + baseString(this) else this
