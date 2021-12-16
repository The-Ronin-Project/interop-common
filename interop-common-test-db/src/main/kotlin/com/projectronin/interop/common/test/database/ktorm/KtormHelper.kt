package com.projectronin.interop.common.test.database.ktorm

import com.projectronin.interop.common.test.database.liquibase.LiquibaseTest
import org.ktorm.database.Database

/**
 * Common helpers for Ktorm.
 */
class KtormHelper {
    companion object {
        /**
         * Creates a Ktorm [Database] for the provided [dbUrl], or the default one used by [LiquibaseTest].
         */
        fun database(dbUrl: String = LiquibaseTest.DEFAULT_DB_URL) = Database.connect(dbUrl)
    }
}
