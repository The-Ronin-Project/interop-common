plugins {
    alias(libs.plugins.interop.junit)
}

dependencies {
    api(libs.jackson.annotations)
    api(libs.jackson.databind)
    api(libs.jackson.datatype.jsr310)
    api(libs.jackson.module.kotlin)
}
