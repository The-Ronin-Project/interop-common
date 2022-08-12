plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    implementation(libs.ktor.client.core)

    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
}
