dependencies {
    // kotest 6.x.x 는 kotlin 2.x 이상 부터 가능
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testImplementation("io.kotest:kotest-framework-engine:5.9.1")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")

    testImplementation(kotlin("test"))
}
