pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "adapter"

includeBuild("../domain")
includeBuild("../application")
