plugins {
    alias(libs.plugins.interop.junit)
}

dependencies {
    api(libs.ktorm.core)
    implementation(libs.uuid.creator)

    testImplementation(platform(libs.testcontainers.bom))
    testImplementation("org.testcontainers:testcontainers")
    testImplementation(libs.liquibase.core)
    testImplementation(libs.rider.junit5)
    testImplementation(libs.mockk)
    testImplementation(project(":interop-common-test-db"))
    testRuntimeOnly("org.testcontainers:mysql")
    testRuntimeOnly(libs.mysql.connector.java)
}
