pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") { from(files("../app/gradle/libs.versions.toml")) }
    }
}

rootProject.name = "domain"
