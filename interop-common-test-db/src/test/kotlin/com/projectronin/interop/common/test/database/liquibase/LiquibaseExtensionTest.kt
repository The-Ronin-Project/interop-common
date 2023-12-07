package com.projectronin.interop.common.test.database.liquibase

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import java.util.Optional
import kotlin.reflect.full.IllegalCallableAccessException
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class LiquibaseExtensionTest {
    private val extension = LiquibaseExtension()

    @Test
    fun `no test class`() {
        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.empty()
            }

        extension.beforeEach(context)
    }

    @Test
    fun `no test instances`() {
        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(NoLiquibaseTest::class.java)
                every { testInstances } returns Optional.empty()
            }

        extension.beforeEach(context)
    }

    @Test
    fun `no test instance for class`() {
        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(NoLiquibaseTest::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(NoLiquibaseTest::class.java) } returns Optional.empty()
                        },
                    )
            }

        extension.beforeEach(context)
    }

    @Test
    fun `no liquibase test found`() {
        val instance = NoLiquibaseTest()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(NoLiquibaseTest::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(NoLiquibaseTest::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        assertNull(instance.connectionHolder)
    }

    @Test
    fun `finds liquibase test on current class`() {
        val instance = LiquibaseTestOnClass()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnClass::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        assertEquals(getExpectedConnectionHolder(), instance.connectionHolder)
    }

    @Test
    fun `finds liquibase test on superclass`() {
        val instance = LiquibaseTestOnSuperClass()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnSuperClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnSuperClass::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        assertEquals(getExpectedConnectionHolder(), instance.connectionHolder)
    }

    @Test
    fun `no marked member property`() {
        val instance = LiquibaseTestWithNoMarkedMember()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestWithNoMarkedMember::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestWithNoMarkedMember::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        assertNull(instance.connectionHolder)
    }

    @Test
    fun `non-valid marked member property`() {
        val instance = LiquibaseTestWithDifferentlyMarkedMember()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestWithDifferentlyMarkedMember::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestWithDifferentlyMarkedMember::class.java) } returns
                                Optional.of(
                                    instance,
                                )
                        },
                    )
            }

        extension.beforeEach(context)
        assertNull(instance.connectionHolder)
    }

    @Test
    fun `no accessible member property`() {
        val instance = LiquibaseTestWithPrivateMarkedMember()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestWithPrivateMarkedMember::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestWithPrivateMarkedMember::class.java) } returns
                                Optional.of(
                                    instance,
                                )
                        },
                    )
            }

        assertThrows<IllegalCallableAccessException> { extension.beforeEach(context) }
    }

    @Test
    fun `no mutable member property`() {
        val instance = LiquibaseTestWithImmutableMarkedMember()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestWithImmutableMarkedMember::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestWithImmutableMarkedMember::class.java) } returns
                                Optional.of(
                                    instance,
                                )
                        },
                    )
            }

        extension.beforeEach(context)
        assertNull(instance.connectionHolder)
    }

    @Test
    fun `sets same ConnectionHolder on subsequent calls`() {
        val instance = LiquibaseTestOnSuperClass()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnSuperClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnSuperClass::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        val expectedConnectionHolder = getExpectedConnectionHolder()
        assertEquals(expectedConnectionHolder, instance.connectionHolder)

        // We're going to verify this by actually using a totally different instance of the same class
        val instance2 = LiquibaseTestOnSuperClass()

        val context2 =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnSuperClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnSuperClass::class.java) } returns Optional.of(instance2)
                        },
                    )
            }

        extension.beforeEach(context2)
        assertEquals(expectedConnectionHolder, instance2.connectionHolder)
        assertTrue(instance.connectionHolder === instance2.connectionHolder)
    }

    @Test
    fun `creates new connection if first one is closed`() {
        val instance = LiquibaseTestOnSuperClass()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnSuperClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnSuperClass::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        assertEquals(getExpectedConnectionHolder(), instance.connectionHolder)

        // Close the initial connection
        instance.connectionHolder.connection.close()

        // We're going to verify this by actually using a totally different instance of the same class
        val instance2 = LiquibaseTestOnSuperClass()

        val context2 =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnSuperClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnSuperClass::class.java) } returns Optional.of(instance2)
                        },
                    )
            }

        extension.beforeEach(context2)
        assertEquals(getExpectedConnectionHolder(), instance2.connectionHolder)
        assertTrue(instance.connectionHolder !== instance2.connectionHolder)
    }

    @Test
    fun `sets connection to auto-commit`() {
        val instance = LiquibaseTestOnClass()

        val context =
            mockk<ExtensionContext> {
                every { testClass } returns Optional.of(LiquibaseTestOnClass::class.java)
                every { testInstances } returns
                    Optional.of(
                        mockk {
                            every { findInstance(LiquibaseTestOnClass::class.java) } returns Optional.of(instance)
                        },
                    )
            }

        extension.beforeEach(context)
        assertTrue(instance.connectionHolder.connection.autoCommit)
    }

    private fun getExpectedConnectionHolder(): ConnectionHolder? {
        val connectionHolderProperty = extension::class.memberProperties.find { it.name == "connectionHolder" }!!
        connectionHolderProperty.getter.isAccessible = true
        return connectionHolderProperty.getter.call(extension) as ConnectionHolder?
    }
}

class NoLiquibaseTest {
    @DBRiderConnection
    var connectionHolder: ConnectionHolder? = null
}

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class LiquibaseTestOnClass {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder
}

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
abstract class SuperClassWithLiquibaseTest {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder
}

class LiquibaseTestOnSuperClass : SuperClassWithLiquibaseTest()

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class LiquibaseTestWithNoMarkedMember {
    var connectionHolder: ConnectionHolder? = null
}

annotation class AnotherAnnotation

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class LiquibaseTestWithDifferentlyMarkedMember {
    @AnotherAnnotation
    var connectionHolder: ConnectionHolder? = null
}

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class LiquibaseTestWithPrivateMarkedMember {
    @DBRiderConnection
    private var connectionHolder: ConnectionHolder? = null
}

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class LiquibaseTestWithImmutableMarkedMember {
    @DBRiderConnection
    val connectionHolder: ConnectionHolder? = null
}
