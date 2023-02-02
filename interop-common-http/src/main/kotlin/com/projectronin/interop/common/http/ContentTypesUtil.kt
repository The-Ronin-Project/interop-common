package com.projectronin.interop.common.http

import io.ktor.http.ContentType

/**
 * ContentType for application/fhir+json
 */
val ContentType.Application.FhirJson: ContentType
    get() = ContentType("application", "fhir+json")
