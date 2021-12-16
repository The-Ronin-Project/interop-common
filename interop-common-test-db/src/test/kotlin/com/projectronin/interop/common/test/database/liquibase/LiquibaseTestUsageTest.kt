package com.projectronin.interop.common.test.database.liquibase

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import com.projectronin.interop.common.test.database.ktorm.KtormHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@LiquibaseTest(changeLog = "/liquibase/test-database.yaml")
@DataSet(cleanBefore = true)
class LiquibaseTestUsageTest {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `processes DBUnit`() {
        val database = KtormHelper.database()
        val samples = database.from(SampleTable).select()
        assertEquals(2, samples.totalRecords)
    }
}

object SampleTable : Table<Nothing>("sample_table") {
    val id = int("sample_table_id").primaryKey()
    val name = varchar("name")
    val value = varchar("value")
}
