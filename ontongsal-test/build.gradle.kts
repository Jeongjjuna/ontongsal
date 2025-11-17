plugins {
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // springboot 통합 테스트 라이브러리 -> Junit5, AssertJ, Mockito 등 다양한 라이브러리 제공
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // kotlin 언어로 junit5를 사용할 수 있게 제공
    // 어떻게? -> ex) 코틀린의 nullable 특징을 활용할 수 있는 코틀린 Junit 메서드 제공
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // SpringBoot 환경에서 junit 사용을 위한 런처(명시적으로 런처를 지정해주는 것과 같다)
    // 단순 Java/Kotlin 프로젝트라면 필요없을 수 있지만, SpringBoot 와 함께 쓴다면 있어야함.(이유는 블로그 학습에 정리)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
