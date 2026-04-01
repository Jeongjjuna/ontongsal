pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "app"

includeBuild("../domain")
includeBuild("../application")
includeBuild("../adapter")
