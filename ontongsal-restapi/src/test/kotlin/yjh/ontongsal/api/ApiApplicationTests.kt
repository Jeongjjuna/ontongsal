package yjh.ontongsal.api

import io.kotest.core.annotation.DisplayName
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@DisplayName("[통합 테스트]")
@Disabled
@ActiveProfiles("local-test")
@SpringBootTest
class ApiApplicationTests {

    @Disabled
    @Test
    fun contextLoads() {
    }
}
