package com.projectronin.interop.common.ktorm

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import com.projectronin.interop.common.test.database.ktorm.KtormHelper
import com.projectronin.interop.common.test.database.liquibase.LiquibaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class DatabaseUtilsTest {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder

    interface ADataObject : Entity<ADataObject> {
        companion object : Entity.Factory<ADataObject>()

        var id: Int
        var whatDoesSheDo: String
    }

    object ATable : Table<ADataObject>("tables") {
        var id = int("tables_id").bindTo { it.id }.primaryKey()
        var whatDoesSheDo = varchar("her_job").bindTo { it.whatDoesSheDo }
    }

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `singleValueLookup works`() {
        val result = KtormHelper.database().singleValueLookup<String, ADataObject>("tables!!!!", ATable.whatDoesSheDo)
        assertEquals(2, result?.id)
        assertEquals("tables!!!!", result?.whatDoesSheDo)
    }

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `singleValueLookup works for null`() {
        assertNull(KtormHelper.database().singleValueLookup<String, ADataObject>("3", ATable.whatDoesSheDo))
    }

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `valueLookup works`() {
        val result = KtormHelper.database().valueLookup<String, ADataObject>("tables!!!!", ATable.whatDoesSheDo)
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(2, result.first().id)
        assertEquals("tables!!!!", result.first().whatDoesSheDo)
    }

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `valueLookup works nothing`() {
        val result = KtormHelper.database().valueLookup<String, ADataObject>("3", ATable.whatDoesSheDo)
        assertNotNull(result)
        assertEquals(0, result.size)
    }
}
