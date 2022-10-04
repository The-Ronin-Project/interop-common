plugins {
    id("com.projectronin.interop.gradle.junit")
    id("com.projectronin.interop.gradle.spring")
}

dependencies {
    implementation(project(":interop-common-jackson"))
    implementation(project(":interop-common"))

    implementation("org.springframework:spring-context")

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.jackson)

    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
}
