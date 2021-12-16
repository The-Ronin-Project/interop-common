package com.projectronin.interop.common.test.database.dbrider

/**
 * Annotates a test property as a [com.github.database.rider.core.api.connection.ConnectionHolder] for use by DBRider.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class DBRiderConnection
