package com.projectronin.interop.common.test.database.ktorm

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import com.projectronin.interop.common.test.database.liquibase.LiquibaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@LiquibaseTest(
    changeLog = "liquibase/test-database.yaml",
    dbUrl = "jdbc:tc:mysql:8.0://localhost:3306/differentdatabase"
)
class KtormHelperNonDefaultTest {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder

    @Test
    fun `creates DB for non-default URL`() {
        Assertions.assertNotNull(KtormHelper.database("jdbc:tc:mysql:8.0://localhost:3306/differentdatabase"))
    }
}
