plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "wemade.ontongsal"
version = "0.0.1-SNAPSHOT"
description = "spring-modulith"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

extra["springModulithVersion"] = "2.0.6"

dependencies {
    // spring-webmvc
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")

    // spring-validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // spring-modulith
	implementation("org.springframework.modulith:spring-modulith-starter-core")

    // spring-transaction
    implementation("org.springframework:spring-tx")

    // spring-exposed
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:1.3.0")
    implementation("com.h2database:h2")

    // kotlin logging
    implementation("io.github.oshai:kotlin-logging-jvm:8.0.01")

	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
