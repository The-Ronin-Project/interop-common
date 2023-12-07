package com.projectronin.interop.common.hl7

enum class ProcessingID(val abbreviation: String) {
    DEBUGGING("D"),
    PRODUCTION("P"),
    TRAINING("T"),
    NONPRODUCTIONTESTING("N"),
    VALIDATION("V"),
}
