package yjh.ontongsal.testing

import org.junit.jupiter.api.DisplayName
import yjh.ontongsal.testing.config.IntegrationTest
import kotlin.test.Test

@DisplayName("[통합테스트] SpringTestingApplication")
class SpringTestingApplicationTest : IntegrationTest() {

    @Test
    fun `컨텍스트 로드 테스트`() {
        // 스프링 컨텍스트가 정상적으로 뜨는지 확인
    }
}
