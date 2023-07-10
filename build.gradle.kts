plugins {
    alias(libs.plugins.interop.junit) apply false
    alias(libs.plugins.interop.spring) apply false
    alias(libs.plugins.interop.publish) apply false
    alias(libs.plugins.interop.version)
}

subprojects {
    apply(plugin = "com.projectronin.interop.gradle.publish")
}
