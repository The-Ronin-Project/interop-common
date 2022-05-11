dependencies {
    implementation(libs.ktorm.core)

    // These dependencies are test implementations generally, but since we are providing test utilities from this project, they all need to be considered implementation
    implementation(platform(libs.testcontainers.bom))
    implementation("org.testcontainers:junit-jupiter")

    implementation(libs.liquibase.core)
    implementation(libs.rider.junit5)

    testImplementation(libs.mockk)
}
