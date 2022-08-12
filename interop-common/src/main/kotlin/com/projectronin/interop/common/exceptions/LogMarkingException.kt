package com.projectronin.interop.common.exceptions

import org.slf4j.Marker

abstract class LogMarkingException(message: String) : Exception(message) {
    abstract val logMarker: Marker
}
