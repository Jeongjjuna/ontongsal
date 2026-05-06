plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"

    id("org.springframework.boot") version "3.4.11"
    id("io.spring.dependency-management") version "1.1.7"
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.github.oshai:kotlin-logging-jvm:8.0.01")

    runtimeOnly("com.mysql:mysql-connector-j") // 운영용
    runtimeOnly("com.h2database:h2")           // 로컬용

    // springboot 통합 테스트 라이브러리 -> Junit5, AssertJ, Mockito 등 다양한 라이브러리 제공
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Spring Security 테스트 지원 라이브러리 -> @WithMockUser, SecurityMockMvcRequestPostProcessors 등
    // 인증/인가가 필요한 Controller 테스트를 쉽게 작성하도록 도와줌
    testImplementation("org.springframework.security:spring-security-test")

    // testcontainer 라이브러리
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    // kotlin 언어로 junit5를 사용할 수 있게 제공
    // 어떻게? -> ex) 코틀린의 nullable 특징을 활용할 수 있는 코틀린 Junit 메서드 제공
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // SpringBoot 환경에서 junit 사용을 위한 런처(명시적으로 런처를 지정해주는 것과 같다)
    // 단순 Java/Kotlin 프로젝트라면 필요없을 수 있지만, SpringBoot 와 함께 쓴다면 있어야함.(이유는 블로그 학습에 정리)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
