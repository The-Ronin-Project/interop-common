package com.projectronin.interop.common.resource

/**
 * Enumeration of the supported Resource Type. Resource Types may be FHIR but are not required to be and should represent all distinct resources InterOps is responsible for handling.
 */
enum class ResourceType {
    BUNDLE,
    PATIENT,
    APPOINTMENT,
    PRACTITIONER_ROLE,
    PRACTITIONER,
    OUTBOUND_MESSAGE,
    CONDITION,
    LOCATION,
    OBSERVATION,
    CARE_PLAN,
    CARE_TEAM,
    COMMUNICATION,
    CONCEPT_MAP,
    DIAGNOSTIC_REPORT,
    ENCOUNTER,
    MEDICATION,
    MEDICATION_REQUEST,
    MEDICATION_STATEMENT,
    ORGANIZATION
}
