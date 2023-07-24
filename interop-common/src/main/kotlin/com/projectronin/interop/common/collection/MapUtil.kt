package com.projectronin.interop.common.collection

/**
 * Maps the values in this Map based off the [transform] returning a new Map with the same keys and values as determined by the transform.
 * Any keys associated to values that transform to null will be excluded from the returned Map.
 */
fun <K, V, R> Map<out K, V>.mapValuesNotNull(transform: (Map.Entry<K, V>) -> R?): Map<K, R> =
    this.mapNotNull { entry ->
        transform(entry)?.let { entry.key to it }
    }.associate { it }

/**
 * Maps all the values into a List containing the single element currently in the value.
 */
fun <K, V> Map<K, V>.mapListValues(): Map<K, List<V>> = mapValues { (_, v) -> listOf(v) }
