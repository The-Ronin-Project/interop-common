plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    api(libs.ktorm.core)
    implementation(libs.uuid.creator)

    testImplementation(libs.mockk)
}
