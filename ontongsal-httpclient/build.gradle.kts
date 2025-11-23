plugins {
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}


dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // spring webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}
