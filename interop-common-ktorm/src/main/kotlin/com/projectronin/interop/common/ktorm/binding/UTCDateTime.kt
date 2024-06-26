package com.projectronin.interop.common.ktorm.binding

import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * Reads a time into a UTC-based OffsetDateTime.
 */
fun BaseTable<*>.utcDateTime(name: String): Column<OffsetDateTime> = registerColumn(name, UTCDateTimeSqlType)

/**
 * SqlType supporting storing an OffsetDateTime relative to UTC.
 */
object UTCDateTimeSqlType : SqlType<OffsetDateTime>(Types.TIMESTAMP, "datetime") {
    override fun doGetResult(
        rs: ResultSet,
        index: Int,
    ): OffsetDateTime? {
        val timestamp = rs.getTimestamp(index)
        return timestamp?.let {
            OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC)
        }
    }

    override fun doSetParameter(
        ps: PreparedStatement,
        index: Int,
        parameter: OffsetDateTime,
    ) {
        ps.setTimestamp(index, Timestamp.from(parameter.toInstant()))
    }
}
