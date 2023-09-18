package com.projectronin.interop.common.ktorm.dao

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.projectronin.interop.common.test.database.dbrider.DBRiderConnection
import com.projectronin.interop.common.test.database.ktorm.KtormHelper
import com.projectronin.interop.common.test.database.liquibase.LiquibaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@LiquibaseTest(changeLog = "liquibase/test-database.yaml")
class BaseInteropDAOTest {
    @DBRiderConnection
    lateinit var connectionHolder: ConnectionHolder
    interface aDO : Entity<aDO> {
        companion object : Entity.Factory<aDO>()
        var id: Int
        var whatDoesSheDo: String
    }

    object table : Table<aDO>("tables") {
        var id = int("tables_id").bindTo { it.id }.primaryKey()
        var whatDoesSheDo = varchar("her_job").bindTo { it.whatDoesSheDo }
    }
    class NotBasedInteropDAO(database: Database) : BaseInteropDAO<aDO, Int>(database) {
        // override val baseTable = table
        override val primaryKeyColumn = table.id
    }

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `getByID works`() {
        val dao = NotBasedInteropDAO(KtormHelper.database())
        val result = dao.getByID(1)
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("tables", result?.whatDoesSheDo)
    }

    @Test
    @DataSet(value = ["/dbunit/Tables.yaml"])
    fun `getByID returns null`() {
        val dao = NotBasedInteropDAO(KtormHelper.database())
        assertNull(dao.getByID(3))
    }
}
