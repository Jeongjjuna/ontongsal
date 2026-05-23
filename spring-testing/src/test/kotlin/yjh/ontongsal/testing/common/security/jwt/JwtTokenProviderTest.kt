package yjh.ontongsal.testing.common.security.jwt

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import yjh.ontongsal.testing.common.web.security.TestingUserDetails
import yjh.ontongsal.testing.common.web.security.jwt.JwtTokenProvider
import yjh.ontongsal.testing.common.web.security.jwt.exception.InvalidJwtException
import yjh.ontongsal.testing.config.IntegrationTest
import java.time.Duration
import kotlin.test.Test

@DisplayName("[통합테스트] JwtTokenProvider")
class JwtTokenProviderTest @Autowired constructor(
    private val jwtTokenProvider: JwtTokenProvider,
) : IntegrationTest() {

    @Test
    fun `토큰 생성 성공`() {
        // given
        val userId = 999L
        val email = "test@test.com"
        val role = "ADMIN"

        // when
        val token = jwtTokenProvider.generateToken(
            userId,
            email,
            role,
            Duration.ofMinutes(10)
        )

        // then
        assertNotNull(token)
        assertTrue(token.isNotBlank())
    }


    @Test
    fun `토큰으로 Authentication 생성 성공`() {
        // given
        val token = jwtTokenProvider.generateToken(
            1L,
            "test@test.com",
            "USER",
            Duration.ofMinutes(10)
        )

        // when
        val authentication: Authentication = jwtTokenProvider.getAuthentication(token)

        // then
        assertThat(authentication.principal).isInstanceOf(TestingUserDetails::class.java)
        val principal: TestingUserDetails = authentication.principal as TestingUserDetails
        assertThat(principal.userId).isEqualTo(1L)
        assertThat(principal.username).isEqualTo("test@test.com")
    }

    @Test
    fun `잘못된 토큰이면 예외 발생`() {
        // given
        val invalidToken = "invalid.jwt.token"

        // when & then
        assertThatThrownBy { jwtTokenProvider.getAuthentication(invalidToken) }
            .isInstanceOf(InvalidJwtException::class.java)
    }

    @Test
    fun `만료된 토큰이면 예외 발생`() {
        // given
        val token = jwtTokenProvider.generateToken(
            999L,
            "test@test.com",
            "ADMIN",
            Duration.ofMillis(1)
        )

        Thread.sleep(5)

        // when & then
        assertThatThrownBy { jwtTokenProvider.getAuthentication(token) }
            .isInstanceOf(InvalidJwtException::class.java)
    }
}
