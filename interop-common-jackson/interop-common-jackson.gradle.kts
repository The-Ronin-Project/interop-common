plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)
}
