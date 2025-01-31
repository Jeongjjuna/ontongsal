dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")
}
