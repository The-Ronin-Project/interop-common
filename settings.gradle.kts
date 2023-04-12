rootProject.name = "interop-common-build"

include("interop-common")
include("interop-common-http")
include("interop-common-jackson")
include("interop-common-ktorm")
include("interop-common-test-db")

for (project in rootProject.children) {
    project.buildFileName = "${project.name}.gradle.kts"
}

pluginManagement {
    plugins {
        id("com.projectronin.interop.gradle.base") version "3.0.0"
        id("com.projectronin.interop.gradle.publish") version "3.0.0"
        id("com.projectronin.interop.gradle.version") version "3.0.0"
    }

    repositories {
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-snapshots/")
            mavenContent {
                snapshotsOnly()
            }
        }
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-releases/")
            mavenContent {
                releasesOnly()
            }
        }
        maven {
            url = uri("https://repo.devops.projectronin.io/repository/maven-public/")
            mavenContent {
                releasesOnly()
            }
        }
        mavenLocal()
        gradlePluginPortal()
    }
}
