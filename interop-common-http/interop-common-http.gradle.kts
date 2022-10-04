plugins {
    id("com.projectronin.interop.gradle.junit")
    id("com.projectronin.interop.gradle.spring")
}

dependencies {
    api(project(":interop-common-jackson"))
    api(project(":interop-common"))

    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.client.logging)
    api(libs.ktor.serialization.jackson)

    implementation("org.springframework:spring-context")

    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
}
