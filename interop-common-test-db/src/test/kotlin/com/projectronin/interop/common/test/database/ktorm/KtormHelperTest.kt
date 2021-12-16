package com.projectronin.interop.common.test.database.ktorm

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import com.projectronin.interop.common.test.database.liquibase.LiquibaseTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class KtormHelperTest {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder

    @Test
    fun `creates DB for default URL`() {
        assertNotNull(KtormHelper.database())
    }
}
