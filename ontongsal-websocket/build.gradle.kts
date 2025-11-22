plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // spring websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // spring redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // spring test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
