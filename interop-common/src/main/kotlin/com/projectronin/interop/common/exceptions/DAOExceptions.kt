package com.projectronin.interop.common.exceptions

import com.projectronin.interop.common.logmarkers.LogMarkers

abstract class DAOExceptions(
    operation: DatabaseOperation,
    databaseName: String,
    extraMsg: String?,
) : LogMarkingException(
        "Database ${operation.name} failed for $databaseName. ${extraMsg?.let { "Error text: $it" } ?: ""}",
    )

class DatabaseInsertFailureException(
    databaseName: String,
    extraMsg: String? = null,
) : DAOExceptions(DatabaseOperation.INSERT, databaseName, extraMsg) {
    override val logMarker = LogMarkers.DATABASE_FAILURE
}

class DatabaseReadFailureException(
    databaseName: String,
    extraMsg: String? = null,
) : DAOExceptions(DatabaseOperation.READ, databaseName, extraMsg) {
    override val logMarker = LogMarkers.DATABASE_FAILURE
}

class DatabaseUpdateFailureException(
    databaseName: String,
    extraMsg: String? = null,
) : DAOExceptions(DatabaseOperation.UPDATE, databaseName, extraMsg) {
    override val logMarker = LogMarkers.DATABASE_FAILURE
}

class DatabaseDeleteFailureException(
    databaseName: String,
    extraMsg: String? = null,
) : DAOExceptions(DatabaseOperation.DELETE, databaseName, extraMsg) {
    override val logMarker = LogMarkers.DATABASE_FAILURE
}

enum class DatabaseOperation {
    INSERT,
    UPDATE,
    READ,
    DELETE,
}
