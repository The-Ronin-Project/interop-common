plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    api(libs.jackson.annotations)
    api(libs.jackson.databind)
    api(libs.jackson.datatype.jsr310)
    api(libs.jackson.module.kotlin)
}
