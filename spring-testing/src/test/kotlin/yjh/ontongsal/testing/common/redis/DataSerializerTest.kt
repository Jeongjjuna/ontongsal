package yjh.ontongsal.testing.common.redis

import com.fasterxml.jackson.core.JsonProcessingException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import yjh.ontongsal.testing.common.dataserializer.DataSerializer
import java.time.Instant
import kotlin.test.Test

@DisplayName("[단위테스트] DataSerializer")
class DataSerializerTest {

    data class UserDto(
        val id: Long,
        val name: String,
        val email: String?,
        val createdAt: Instant? = null,
    )

    @Test
    fun `객체를 JSON 으로 직렬화 한다`() {
        // given
        val user = UserDto(id = 1L, name = "홍길동", email = "hong@example.com")

        //when
        val result = DataSerializer.serialize(user)

        // then
        val expected = """{"id":1,"name":"홍길동","email":"hong@example.com","createdAt":null}"""
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `JSON 을 객체로 역직렬화 한다`() {
        // given
        val json = """{"id":1,"name":"홍길동","email":"hong@example.com","createdAt":null}"""

        // when
        val result = DataSerializer.deserialize(json, UserDto::class.java)

        // then
        assertAll(
            { assertThat(result.id).isEqualTo(1) },
            { assertThat(result.name).isEqualTo("홍길동") },
            { assertThat(result.email).isEqualTo("hong@example.com") },
            { assertThat(result.createdAt).isNull() },
        )
    }

    data class TimeDto(
        val id: Long,
        val occurredAt: Instant,
    )

    @Test
    fun `Instant 를 ISO-8601 문자열로 직렬화 한다`() {
        // given
        val instant = Instant.parse("2024-06-01T12:00:00Z")
        val dto = TimeDto(
            id = 1L,
            occurredAt = instant
        )

        // when
        val result = DataSerializer.serialize(dto)

        // then
        val expected =
            """{"id":1,"occurredAt":"2024-06-01T12:00:00Z"}"""

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `ISO-8601 문자열을 Instant 로 역직렬화 한다`() {
        // given
        val json =
            """{"id":1,"occurredAt":"2024-06-01T12:00:00Z"}"""

        // when
        val result = DataSerializer.deserialize(json, TimeDto::class.java)

        // then
        assertAll(
            { assertThat(result.id).isEqualTo(1L) },
            {
                assertThat(result.occurredAt)
                    .isEqualTo(Instant.parse("2024-06-01T12:00:00Z"))
            }
        )
    }

    @Test
    fun `Instant serialize 후 deserialize 하면 동일해야 한다`() {
        // given
        val original = TimeDto(
            id = 1L,
            occurredAt = Instant.now()
        )
        val json = DataSerializer.serialize(original)

        // when
        val restored = DataSerializer.deserialize(json, TimeDto::class.java)

        // then
        assertThat(restored).isEqualTo(original)
    }

    @Test
    fun `잘못된 JSON 역직렬화시 SerializationException 을 던진다`() {
        // given
        val invalidJson = """{invalid json}"""

        // when & then
        assertThatThrownBy {
            DataSerializer.deserialize(invalidJson, UserDto::class.java)
        }
            .isInstanceOf(JsonProcessingException::class.java)
    }
}
