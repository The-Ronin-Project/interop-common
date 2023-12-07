package com.projectronin.interop.common.test.database.liquibase

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import mu.KotlinLogging
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * JUnit Jupiter extension capable of configuring a Liquibase environment and preparing it for DBRider. This extension
 * should not be manually referenced and should instead be included via the [LiquibaseTest] annotation.
 */
class LiquibaseExtension : BeforeEachCallback, AfterAllCallback {
    private val logger = KotlinLogging.logger { }
    private var connectionProperty: KMutableProperty<*>? = null
    private var connectionHolder: ConnectionHolder? = null

    override fun beforeEach(context: ExtensionContext) {
        val testClass = context.testClass.orElse(null) ?: return
        val testInstance = context.testInstances.map { it.findInstance(testClass).orElse(null) }.orElse(null) ?: return

        if (connectionHolder?.connection?.isClosed == false) {
            // There are multiple scenarios where we get different instances of the class, so we need to ensure we set the
            // connection everytime.
            connectionProperty?.let {
                setDBRiderConnection(it, testInstance)
                return
            }
        }

        logger.info { "Inspecting for Liquibase tests" }
        val liquibaseTest = findLiquibaseTest(testClass.kotlin)
        if (liquibaseTest == null) {
            logger.info { "No Liquibase tests found" }
        } else {
            logger.info { "Setting up connection for ${testClass.name}" }
            connectionProperty = getConnectionProperty(testClass)
            connectionProperty?.let {
                val connection = setupDatabase(liquibaseTest)
                connectionHolder = ConnectionHolder { connection }
                setDBRiderConnection(it, testInstance)
            }
        }
    }

    override fun afterAll(context: ExtensionContext?) {
        logger.info { "Resetting connection" }
        connectionHolder = null
        connectionProperty = null
    }

    private fun findLiquibaseTest(testClass: KClass<*>): LiquibaseTest? {
        val liquibaseTest = testClass.findAnnotation<LiquibaseTest>()
        liquibaseTest?.let { return it }

        return testClass.allSuperclasses.mapNotNull { it.findAnnotation<LiquibaseTest>() }.firstOrNull()
    }

    private fun setupDatabase(liquibaseTest: LiquibaseTest): Connection {
        logger.info { "Setting up liquibase" }

        val connection = DriverManager.getConnection(liquibaseTest.dbUrl)
        val liquibase =
            Liquibase(liquibaseTest.changeLog, ClassLoaderResourceAccessor(), JdbcConnection(connection))
        liquibase.update(Contexts())

        logger.info { "Liquibase setup complete" }

        connection.autoCommit = true
        return connection
    }

    private fun getConnectionProperty(testClass: Class<*>): KMutableProperty<*>? {
        logger.info { "Finding @DBRiderConnection property" }
        return testClass.kotlin.memberProperties.mapNotNull { it as? KMutableProperty<*> }
            .find { it.findAnnotation<DBRiderConnection>() != null }
    }

    private fun setDBRiderConnection(
        connectionProperty: KMutableProperty<*>,
        testInstance: Any,
    ) {
        logger.info { "Wiring connection to ${connectionProperty.name} on $testInstance" }
        connectionProperty.setter.call(testInstance, connectionHolder)
    }
}
