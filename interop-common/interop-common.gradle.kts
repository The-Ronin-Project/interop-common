plugins {
    id("com.projectronin.interop.gradle.junit")
}

dependencies {
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
}
