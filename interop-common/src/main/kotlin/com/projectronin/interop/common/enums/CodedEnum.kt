package com.projectronin.interop.common.enums

interface CodedEnum<T> {
    companion object {
        inline fun <reified T> byCode(code: String) where T : Enum<T>, T : CodedEnum<T> = enumValues<T>().firstOrNull { it.code == code }
    }

    val code: String
}
