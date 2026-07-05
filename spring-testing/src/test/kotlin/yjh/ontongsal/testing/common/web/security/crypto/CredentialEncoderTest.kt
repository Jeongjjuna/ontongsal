package yjh.ontongsal.testing.common.web.security.crypto

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import yjh.ontongsal.testing.config.IntegrationTest
import kotlin.test.Test

@DisplayName("[단위테스트] CredentialEncoder")
class CredentialEncoderTest(
    private val sut: CredentialEncoder,
): IntegrationTest() {

    @Test
    fun `raw password를 hash 하면 해시된 값이 나온다`() {
        // given
        val rawPassword = "password123!"

        // when
        val result = sut.hash(rawPassword)

        // then
        assertNotNull(result)
    }

}