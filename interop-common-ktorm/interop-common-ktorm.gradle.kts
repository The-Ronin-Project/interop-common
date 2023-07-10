plugins {
    alias(libs.plugins.interop.junit)
}

dependencies {
    api(libs.ktorm.core)
    implementation(libs.uuid.creator)

    testImplementation(libs.mockk)
}
