plugins {
    kotlin("jvm") version "2.4.0"
}

group = "yjh.ontongsal"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.4.0")

    // Source: https://mvnrepository.com/artifact/io.arrow-kt/arrow-optics-jvm
    implementation("io.arrow-kt:arrow-optics-jvm:2.2.3")

    testImplementation(kotlin("test"))
}
