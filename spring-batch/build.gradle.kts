import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "2.2.0"

    // 1. spring-batch 수동 설정 : 클래스와 메서드에 open 설정
    kotlin("plugin.spring") version "2.1.0"
}

group = "wemade.ontongsal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 1. spring-batch 수동 설정
    implementation("org.springframework.batch:spring-batch-core:6.0.1")

    // 2. spring-boot autoconfigure

    testImplementation(kotlin("test"))
}


kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "org.springframework.batch.core.launch.support.CommandLineJobOperator"
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        javaParameters = true
    }
}

tasks.test {
    useJUnitPlatform()
}
