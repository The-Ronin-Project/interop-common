package com.projectronin.interop.common.logmarkers

import org.slf4j.Marker
import org.slf4j.MarkerFactory

/**
 * Defines log markers currently setup in DataDog.  In an effort to keep organized, all log markers used
 * in our systems should be defined here, and our monitors in DataDog should be updated to use them.
 */
object LogMarkers {
    /**
     * Exclude this log from the general warn/error alerts.
     */
    val EXCLUDE_FROM_GENERAL_ALERTS: Marker = MarkerFactory.getMarker("EXCLUDE_FROM_GENERAL_ALERTS")

    /**
     * Service unavailable.  Ex: AppOrchard is down.
     */
    val SERVICE_UNAVAILABLE: Marker = MarkerFactory.getMarker("SERVICE_UNAVAILABLE")

    /**
     * Authorization Failure.
     */
    val AUTHORIZATION: Marker = MarkerFactory.getMarker("AUTHORIZATION")

    /**
     * General server failure  Ex: AppOrchard or Aidbox failed with a 500.
     */
    val SERVER_FAILURE: Marker = MarkerFactory.getMarker("SERVER_FAILURE")

    /**
     * Server indicates something is wrong with the client's call. E.g. Failure to send a correctly formatted JSON
     */
    val CLIENT_FAILURE: Marker = MarkerFactory.getMarker("CLIENT_FAILURE")

    /**
     * Within Proxy server, something failed for the tenant server controllers
     */
    val TENANT_SERVER_FAILURE: Marker = MarkerFactory.getMarker("TENANT_SERVER_FAILURE")

    /**
     * Something failed with a database operation, i.e Tenant or EHR config.
     */
    val DATABASE_FAILURE: Marker = MarkerFactory.getMarker("DATABASE_FAILURE")

    /**
     * An exception occurred during an HTTP Request.
     */
    val HTTP_REQUEST_FAILURE: Marker = MarkerFactory.getMarker("HTTP_REQUEST_FAILURE")

    /**
     * Validation warning or failure has occurred.
     */
    val VALIDATION_ISSUE: Marker = MarkerFactory.getMarker("VALIDATION_ISSUE")

    /**
     * An illegal argument was provided to a service.
     */
    val ILLEGAL_ARGUMENT: Marker = MarkerFactory.getMarker("ILLEGAL_ARGUMENT")

    // Some log markers need to reference each other.
    init {
        SERVICE_UNAVAILABLE.add(EXCLUDE_FROM_GENERAL_ALERTS)
        AUTHORIZATION.add(EXCLUDE_FROM_GENERAL_ALERTS)
        VALIDATION_ISSUE.add(EXCLUDE_FROM_GENERAL_ALERTS)
        ILLEGAL_ARGUMENT.add(EXCLUDE_FROM_GENERAL_ALERTS)
    }
}
