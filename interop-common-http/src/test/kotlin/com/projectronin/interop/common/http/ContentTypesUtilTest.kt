package com.projectronin.interop.common.http

import io.ktor.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ContentTypesUtilTest {
    @Test
    fun `FhirJson returns proper ContentType`() {
        assertEquals("application/fhir+json", ContentType.Application.FhirJson.toString())
    }
}
