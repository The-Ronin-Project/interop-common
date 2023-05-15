package com.projectronin.interop.common.exceptions

import com.projectronin.interop.common.logmarkers.LogMarkers
import org.slf4j.Marker

/**
 * An exception indicating an illegal argument was provided to an Interops service. This should be used in place of the
 * standard [IllegalArgumentException] to ensure that the appropriate LogMarkers are included.
 */
class InteropIllegalArgumentException(message: String) : LogMarkingException(message) {
    override val logMarker: Marker = LogMarkers.ILLEGAL_ARGUMENT
}
