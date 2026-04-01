import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.11"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "4.0.0"
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
    implementation("yjh.ontongsal:application")
    implementation("yjh.ontongsal:domain")
    implementation("yjh.ontongsal:adapter")

    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor:4.0.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
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
