package com.projectronin.interop.common.ktorm.binding

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID

class BinaryUUIDTest {
    @Test
    fun `sqlType supports getting null field`() {
        val resultSet =
            mockk<ResultSet> {
                every { getBytes(1) } returns null
                every { wasNull() } returns true
            }

        val uuid = BinaryUUIDSqlType.getResult(resultSet, 1)
        assertNull(uuid)
    }

    @Test
    fun `sqlType supports getting non-null field`() {
        val resultSet =
            mockk<ResultSet> {
                every { getBytes(1) } returns
                    byteArrayOf(
                        18,
                        -90,
                        38,
                        -65,
                        70,
                        -75,
                        76,
                        60,
                        -88,
                        86,
                        25,
                        -105,
                        -42,
                        -6,
                        24,
                        -124,
                    )
                every { wasNull() } returns false
            }

        val uuid = BinaryUUIDSqlType.getResult(resultSet, 1)
        assertEquals(UUID.fromString("12a626bf-46b5-4c3c-a856-1997d6fa1884"), uuid)
    }

    @Test
    fun `sqlType supports writing UUID`() {
        val preparedStatement = mockk<PreparedStatement>(relaxed = true)
        BinaryUUIDSqlType.setParameter(preparedStatement, 1, UUID.fromString("4800969e-8304-4fdf-935c-50c1cf16c968"))

        verify(exactly = 1) {
            preparedStatement.setObject(
                1,
                byteArrayOf(72, 0, -106, -98, -125, 4, 79, -33, -109, 92, 80, -63, -49, 22, -55, 104),
            )
        }
    }

    @Test
    fun `binaryUuid registers column`() {
        val baseTable =
            object : BaseTable<Any>("table") {
                override fun doCreateEntity(
                    row: QueryRowSet,
                    withReferences: Boolean,
                ): Any {
                    TODO("Not yet implemented")
                }
            }

        val spiedBaseTable = spyk(baseTable)

        spiedBaseTable.binaryUuid("binary_uuid")

        verify(exactly = 1) { spiedBaseTable.registerColumn("binary_uuid", BinaryUUIDSqlType) }
    }
}
