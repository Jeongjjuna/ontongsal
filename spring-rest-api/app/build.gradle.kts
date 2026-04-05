import com.epages.restdocs.apispec.gradle.OpenApi3Extension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.restdocs.api.spec)
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
    implementation(libs.springdoc.openapi.webmvc)

    runtimeOnly(libs.mysql.connector.j)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.restdocs.mockmvc)
    testImplementation(libs.restdocs.api.spec.mockmvc)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.springmockk)
    testImplementation(libs.jackson.module.kotlin)
    asciidoctorExt(libs.spring.restdocs.asciidoctor)
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

configure<OpenApi3Extension> {
    title = "Ontongsal API Documentation"
    description = "Spring REST Docs + OpenAPI 3 + Swagger UI"
    version = "0.1.0"
    format = "yaml"
}

tasks.register("processOpenApi") {
    dependsOn("openapi3")
    doLast {
        val file = file("build/api-spec/openapi3.yaml")
        val processed = file.readText().replace(Regex("\\\\\n[ \t]+"), "")
        file.writeText(processed)
    }
}

tasks.register<Copy>("copyOpenApi") {
    dependsOn("processOpenApi")
    from("build/api-spec/openapi3.yaml")
    into("build/resources/main/static/docs")
}

tasks.named("resolveMainClassName") {
    dependsOn("copyOpenApi")
}

tasks.named<BootJar>("bootJar") {
    enabled = true

    dependsOn("copyOpenApi")

    from(tasks.asciidoctor.get().outputDir) {
        into("static/docs")
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}
