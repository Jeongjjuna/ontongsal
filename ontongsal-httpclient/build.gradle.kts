plugins {
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}


dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}
