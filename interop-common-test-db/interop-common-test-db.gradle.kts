plugins {
    alias(libs.plugins.interop.junit)
}

dependencies {
    api(libs.ktorm.core)

    // These dependencies are test implementations generally, but since we are providing test utilities from this project, they all need to be considered implementation
    api(platform(libs.testcontainers.bom))
    api("org.testcontainers:junit-jupiter")

    api(libs.liquibase.core)
    api(libs.rider.junit5)

    testImplementation(libs.mockk)

    testRuntimeOnly(libs.bundles.test.mysql)
}
