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

    // Some log markers need to reference each other.
    init {
        SERVICE_UNAVAILABLE.add(EXCLUDE_FROM_GENERAL_ALERTS)
    }
}
