package com.projectronin.interop.common.logmarkers

import com.projectronin.interop.common.exceptions.ServiceUnavailableException
import org.slf4j.Marker

/**
 * Returns the log marker associated with the given [Throwable] if there is one.  Returns null otherwise.
 */
fun Throwable.getLogMarker(): Marker? {
    return when (this) {
        is ServiceUnavailableException -> LogMarkers.SERVICE_UNAVAILABLE
        else -> null
    }
}
