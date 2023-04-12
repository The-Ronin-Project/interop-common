package com.projectronin.interop.common.ktorm.binding

import com.github.f4b6a3.uuid.UuidCreator
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.util.UUID

/**
 * Reads a UUID from a BINARY field.
 */
fun BaseTable<*>.binaryUuid(name: String): Column<UUID> = registerColumn(name, BinaryUUIDSqlType)

/**
 * SqlType supporting storing a UUID as a BINARY datatype.
 */
object BinaryUUIDSqlType : SqlType<UUID>(Types.BINARY, typeName = "binary") {
    override fun doGetResult(rs: ResultSet, index: Int): UUID? {
        val byteArray = rs.getBytes(index)
        return byteArray?.let { UuidCreator.fromBytes(byteArray) }
    }

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: UUID) {
        ps.setObject(index, UuidCreator.toBytes(parameter))
    }
}
