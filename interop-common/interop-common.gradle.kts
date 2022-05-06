plugins {
    id("com.projectronin.interop.gradle.jackson")
    id("com.projectronin.interop.gradle.junit")
    id("com.projectronin.interop.gradle.spring")
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
}
