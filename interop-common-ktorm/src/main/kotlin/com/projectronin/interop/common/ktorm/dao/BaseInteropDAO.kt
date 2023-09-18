package com.projectronin.interop.common.ktorm.dao

import com.projectronin.interop.common.ktorm.singleValueLookup
import mu.KotlinLogging
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.schema.Column

/***
 * Base class we can use which implements a simple get for a primary key column
 */

abstract class BaseInteropDAO<T : Entity<T>, K : Any>(val database: Database) {
    val logger = KotlinLogging.logger { }

    abstract val primaryKeyColumn: Column<K>

    fun getByID(primaryKeyLookup: K): T? {
        return database.singleValueLookup<K, T>(primaryKeyLookup, primaryKeyColumn)
    }
}
