package com.projectronin.interop.common.test.database.liquibase

import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.configuration.Orthography
import com.github.database.rider.junit5.DBUnitExtension
import org.dbunit.ext.mysql.MySqlDataTypeFactory
import org.dbunit.ext.mysql.MySqlMetadataHandler
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Marks a Liquibase test using the supplied [changeLog] and optional [dbUrl]. If no [dbUrl] is provided, a default
 * MySQL Testcontainer will be used.
 *
 * This Annotation also introduces DBRider and DBUnit, along with some default configuration, to the class. In order to
 * support DBRider, any class with this annotation should expose a
 * [com.github.database.rider.core.api.connection.ConnectionHolder] property annotated with [com.projectronin.interop.common.test.database.dbrider.DBRiderConnection].
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(*[LiquibaseExtension::class, DBUnitExtension::class])
@DBUnit(
    metaDataHandler = MySqlMetadataHandler::class,
    dataTypeFactoryClass = MySqlDataTypeFactory::class,
    caseInsensitiveStrategy = Orthography.LOWERCASE,
    caseSensitiveTableNames = false,
    cacheConnection = false
)
annotation class LiquibaseTest(
    val changeLog: String,
    val dbUrl: String = DEFAULT_DB_URL
) {
    companion object {
        const val DEFAULT_DB_URL = "jdbc:tc:mysql:8.0://localhost:3306/databasename?TC_DAEMON=true"
    }
}
