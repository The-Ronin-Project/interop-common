package com.projectronin.interop.common.exceptions

/**
 * Exception indicating that a service was unavailable.
 */
class ServiceUnavailableException(service: String, message: String) : Exception("$service: $message")
