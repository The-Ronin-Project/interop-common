package com.projectronin.interop.common.logmarkers

import com.projectronin.interop.common.exceptions.LogMarkingException
import org.slf4j.Marker

/**
 * Returns the log marker associated with the given [Throwable] if there is one.  Returns null otherwise.
 */
fun Throwable.getLogMarker(): Marker? {
    return when (this) {
        is LogMarkingException -> this.logMarker
        else -> null
    }
}
