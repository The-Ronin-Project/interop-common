plugins {
    id("com.projectronin.interop.gradle.ktorm")
    id("com.projectronin.interop.gradle.mockk")
}

// These dependencies are test implementations generally, but since we are providing test utilities from this project, they all need to be considered implementation
dependencies {
    // Test Containers
    implementation("org.testcontainers:junit-jupiter")

    // Database Rider
    implementation("com.github.database-rider:rider-junit5:1.32.3")

    // Liquibase
    implementation("org.liquibase:liquibase-core:4.9.1")
}
