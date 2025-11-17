plugins {
    /* kotlin + jvm */
    kotlin("jvm") version "1.9.25"

    /* kotlin + spring */
    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.jpa") version "1.9.25" apply false

    /* restdocs */
    id("org.asciidoctor.jvm.convert") version "4.0.5" apply false

    /* ktlint */
    id("org.jlleitschuh.gradle.ktlint") version "11.4.0" apply false

    /* springboot */
    id("org.springframework.boot") version "3.4.11" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "yjh.ontongsal"
version = "0.0.1-SNAPSHOT"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    /**
     * 코틀린 컴파일러를 통해 빌드할 때, JDK Bytecode 생성을 위해 JDK 를 사용하고,
     * 이때 JDK21 버전을 사용하도록 명시합니다.
     * required plugin : "org.jetbrains.kotlin.jvm"
     */
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}

subprojects {
    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
