import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.asciidoctor)
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

    implementation(libs.spring.boot.starter)

    testImplementation(libs.spring.boot.starter.test)
    asciidoctorExt(libs.spring.restdocs.asciidoctor)
    testImplementation(libs.kotest.runner.junit5)
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
