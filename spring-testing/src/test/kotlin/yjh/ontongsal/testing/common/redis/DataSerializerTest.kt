package yjh.ontongsal.testing.common.redis

import org.junit.jupiter.api.DisplayName
import java.time.Instant
import kotlin.test.Test

@DisplayName("[단위테스트] DataSerializer")
class DataSerializerTest {

    private var sut = DataSerializer()

    data class UserDto(
        val id: Long,
        val name: String,
        val email: String?,
        val createdAt: Instant? = null,
    )

    @Test
    fun `객체를 JSON 으로 직렬화 한다`() {
        val user = UserDto(id = 1L, name = "홍길동", email = "hong@example.com")

        val result = sut.serialize(user)

        println(result)
    }
}
