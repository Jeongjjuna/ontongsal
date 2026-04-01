plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "yjh.ontongsal"

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

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.11")
    }
}

dependencies {
    implementation("yjh.ontongsal:domain")

    // UseCase, Service에 필요한 최소 Spring 의존
    implementation("org.springframework:spring-context")

    // Circuit Breaker
    val resilience4jVersion = "2.3.0"
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:${resilience4jVersion}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")
}
