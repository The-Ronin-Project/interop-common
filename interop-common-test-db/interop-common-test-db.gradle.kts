plugins {
    id("com.projectronin.interop.gradle.ktorm")
    id("com.projectronin.interop.gradle.mockk")
}

// These dependencies are test implementations generally, but since we are providing test utilities from this project, they all need to be considered implementation
dependencies {
    // Test Containers
    implementation("org.testcontainers:junit-jupiter")

    // Database Rider
    implementation(libs.rider.junit5)

    // Liquibase
    implementation(libs.liquibase.core)
}
