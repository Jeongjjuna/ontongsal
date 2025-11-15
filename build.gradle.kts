plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.asciidoctor.jvm.convert") version "4.0.5"
    id("org.jlleitschuh.gradle.ktlint") version "11.4.0"
    id("org.springframework.boot") version "3.4.11"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "yjh.ontongsal"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.asciidoctor.jvm.convert")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

    repositories {
        mavenCentral()
    }

    extra["snippetsDir"] = file("build/generated-snippets")

    dependencies {
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.test {
        outputs.dir(project.extra["snippetsDir"]!!)
    }

    tasks.asciidoctor {
        inputs.dir(project.extra["snippetsDir"]!!)
        dependsOn(tasks.test)
    }

}
