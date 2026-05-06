package yjh.ontongsal.testing

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringTestingApplicationTest {

    @Test
    fun `컨텍스트 로드 테스트`() {
        // 스프링 컨텍스트가 정상적으로 뜨는지 확인
    }
}
