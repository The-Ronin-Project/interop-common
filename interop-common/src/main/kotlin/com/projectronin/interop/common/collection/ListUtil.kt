package com.projectronin.interop.common.collection

/**
 * Maps each element of a list to a value based off the [transform].
 * The resulting map only contains keys whose values are not null.
 * If a list contains non-unique elements, the last non-unique
 * element will replace any previous as a key in the returned map.
 */
fun <E, T> List<E>.associateWithNonNull(transform: (E) -> T?): Map<E, T> {
    @Suppress("UNCHECKED_CAST")
    return this.associateWith { element ->
        transform(element)
    }.filterValues {
        it != null
    } as Map<E, T>
}
