package com.projectronin.interop.common.exceptions

class VendorIdentifierNotFoundException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    constructor(cause: Throwable) : this(null, cause)
}
