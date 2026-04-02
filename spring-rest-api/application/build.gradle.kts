plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
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
        mavenBom(libs.spring.boot.bom.get().toString())
    }
}

dependencies {
    implementation("yjh.ontongsal:domain")

    // UseCase, Service에 필요한 최소 Spring 의존
    implementation(libs.spring.context)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)
}
