plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"

    id("org.springframework.boot") version "3.4.11"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "yjh.ontongsal"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // spring websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // spring redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // spring test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
