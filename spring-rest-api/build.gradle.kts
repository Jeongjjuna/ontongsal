import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"

    id("org.springframework.boot") version "3.4.11"
    id("io.spring.dependency-management") version "1.1.7"

    id("org.asciidoctor.jvm.convert") version "3.3.2"
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

val asciidoctorExt by configurations.creating

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")

    // resilience4j
    val resilience4jVersion = "2.3.0"
    implementation("io.github.resilience4j:resilience4j-spring-boot3:${resilience4jVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // spring test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring rest docs
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor:4.0.0")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:4.0.0")

    // mockk
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    // kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

    // test container
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:mysql")
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks.test {
    outputs.dir(snippetsDir)
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    configurations("asciidoctorExt")
    dependsOn(tasks.test)
}

tasks.named<BootJar>("bootJar") {
    enabled = true

    dependsOn(tasks.asciidoctor)

    from(tasks.asciidoctor.get().outputDir) {
        into("static/docs")
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}
