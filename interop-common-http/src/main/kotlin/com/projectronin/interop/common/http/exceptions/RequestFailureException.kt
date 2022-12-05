package com.projectronin.interop.common.http.exceptions

import com.projectronin.interop.common.exceptions.LogMarkingException
import com.projectronin.interop.common.logmarkers.LogMarkers

class RequestFailureException(cause: Throwable, serverName: String, url: String) : LogMarkingException(
    "Received exception when calling $serverName ($url): ${cause.message}"
) {
    override val logMarker = LogMarkers.HTTP_REQUEST_FAILURE
}
