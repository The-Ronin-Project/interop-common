package com.projectronin.interop.common.jackson

import kotlin.reflect.KClass

object JacksonUtil {
    private val mapper = JacksonManager.objectMapper

    /**
     * Deserializes a json array into a List<> of [clazz].
     */
    fun <T : Any> readJsonList(
        json: String,
        clazz: KClass<T>,
    ): List<T> {
        val type = mapper.typeFactory.constructCollectionType(List::class.java, clazz.java)
        return mapper.readValue(json, type)
    }

    /**
     * Deserializes a single json object of type [clazz]
     */
    fun <T : Any> readJsonObject(
        json: String,
        clazz: KClass<T>,
    ): T {
        return mapper.readValue(json, clazz.java)
    }

    /**
     * Wrapper around JacksonManager.objectMapper.writeValueAsString
     */
    fun writeJsonValue(value: Any): String {
        return mapper.writeValueAsString(value)
    }
}
