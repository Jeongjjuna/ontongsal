plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.asciidoctor.jvm.convert")
}

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // jpa
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")

    // spring test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring rest docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

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

extra["snippetsDir"] = file("build/generated-snippets")

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}
