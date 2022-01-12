rootProject.name = "interop-common-build"

include("interop-common")
include("interop-common-jackson")
include("interop-common-test-db")

for (project in rootProject.children) {
    project.buildFileName = "${project.name}.gradle.kts"
}

pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/projectronin/package-repo")
            credentials {
                username = System.getenv("PACKAGE_USER")
                password = System.getenv("PACKAGE_TOKEN")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
