package com.projectronin.interop.common.ktorm

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.entity.Entity
import org.ktorm.schema.Column
import org.ktorm.schema.Table

/***
 * Same as [valueLookup] but ensures only one entry is returned
 */
fun <K : Any, T : Entity<T>> Database.singleValueLookup(
    lookupValue: K,
    column: Column<K>,
): T? {
    return this.valueLookup<K, T>(lookupValue, column).singleOrNull()
}

/***
 * Will attempt to find all entries in the [Database] where the [column] matches the [lookupValue].
 * Will throw an error if the table the column is defined in is not the expected return type.
 */
fun <K : Any, T : Entity<T>> Database.valueLookup(
    lookupValue: K,
    column: Column<K>,
): List<T> {
    val table = column.table as Table<T>
    return this.from(table)
        .select()
        .where(column eq lookupValue)
        .map { table.createEntity(it) }
}
