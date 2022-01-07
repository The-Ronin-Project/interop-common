package com.projectronin.interop.common.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode

/**
 * Reads the supplied [fieldName] from the node and returns the data as a String if present, or null.
 */
fun JsonNode.getAsTextOrNull(fieldName: String): String? = this.get(fieldName)?.asText()

/**
 * Reads the supplied [fieldName] from the node and returns the data as an Int if present, or null.
 */
fun JsonNode.getAsIntOrNull(fieldName: String): Int? = this.get(fieldName)?.asInt()

/**
 * Reads the supplied [fieldName] from the node and returns the data as a Double if present, or null.
 */
fun JsonNode.getAsDoubleOrNull(fieldName: String): Double? = this.get(fieldName)?.asDouble()

/**
 * Reads the supplied [fieldName] from the node and returns the data as the requested type [T] using the [currentParser].
 */
inline fun <reified T> JsonNode.getAs(fieldName: String, currentParser: JsonParser): T =
    this.getAsOrNull(fieldName, currentParser)!!

/**
 * Reads the supplied [fieldName] from the node and returns the data as the requested type [T] using the [currentParser] if present, or null.
 */
inline fun <reified T> JsonNode.getAsOrNull(fieldName: String, currentParser: JsonParser): T? =
    this.get(fieldName)?.readValueAs(currentParser, T::class.java)

/**
 * Reads the supplied [fieldName] from the node and returns the data as a List of the requested type [T] using the [currentParser] if present, or an empty List.
 */
inline fun <reified T> JsonNode.getAsList(fieldName: String, currentParser: JsonParser): List<T> {
    val parser = this.get(fieldName)?.traverse() ?: return listOf()
    parser.codec = currentParser.codec
    return parser.readValueAs(object : TypeReference<List<T>>() {})
}

/**
 * Reads the current value as type [T] using the [currentParser].
 */
fun <T> JsonNode.readValueAs(currentParser: JsonParser, clazz: Class<T>): T {
    val parser = this.traverse()
    parser.codec = currentParser.codec
    return parser.readValueAs(clazz)
}

/**
 * Returns the name of all fields on the node starting with the [prefix].
 */
fun JsonNode.fieldsStartingWith(prefix: String): List<String> =
    this.fieldNames().asSequence().filter { it.startsWith(prefix) }.toList()
