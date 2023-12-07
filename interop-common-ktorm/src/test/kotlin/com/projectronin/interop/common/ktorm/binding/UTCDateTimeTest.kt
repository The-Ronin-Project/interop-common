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
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

class UTCDateTimeTest {
    @Test
    fun `sqlType supports getting null field`() {
        val resultSet =
            mockk<ResultSet> {
                every { getTimestamp(1) } returns null
                every { wasNull() } returns true
            }

        val offsetDateTime = UTCDateTimeSqlType.getResult(resultSet, 1)
        assertNull(offsetDateTime)
    }

    @Test
    fun `sqlType supports getting non-null field`() {
        val resultSet =
            mockk<ResultSet> {
                every { getTimestamp(1) } returns
                    Timestamp.from(
                        LocalDateTime.of(2023, 4, 10, 11, 25, 30, 0).toInstant(
                            ZoneOffset.ofHours(3),
                        ),
                    )
                every { wasNull() } returns false
            }

        val offsetDateTime = UTCDateTimeSqlType.getResult(resultSet, 1)
        assertEquals(OffsetDateTime.of(2023, 4, 10, 8, 25, 30, 0, ZoneOffset.UTC), offsetDateTime)
    }

    @Test
    fun `sqlType supports writing OffsetDateTime`() {
        val preparedStatement = mockk<PreparedStatement>(relaxed = true)
        UTCDateTimeSqlType.setParameter(
            preparedStatement,
            1,
            OffsetDateTime.of(2023, 4, 10, 8, 25, 30, 0, ZoneOffset.ofHours(-7)),
        )

        verify(exactly = 1) {
            preparedStatement.setTimestamp(
                1,
                Timestamp.from(LocalDateTime.of(2023, 4, 10, 15, 25, 30, 0).toInstant(ZoneOffset.UTC)),
            )
        }
    }

    @Test
    fun `utcDateTime registers column`() {
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

        spiedBaseTable.utcDateTime("utc_dt_tm")

        verify(exactly = 1) { spiedBaseTable.registerColumn("utc_dt_tm", UTCDateTimeSqlType) }
    }
}
