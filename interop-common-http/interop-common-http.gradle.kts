plugins {
    alias(libs.plugins.interop.junit)
    alias(libs.plugins.interop.spring)
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
    implementation(platform(libs.spring.boot.parent))
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockwebserver)
}
