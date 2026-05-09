package yjh.ontongsal.testing.common.redis

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.exception.SerializationException

private val logger = KotlinLogging.logger {}

@Component
class DataSerializer {

    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    /**
     * Serialize object to JSON string.
     *
     * @throws SerializationException when serialization fails
     */
    fun serialize(obj: Any): String {
        return try {
            objectMapper.writeValueAsString(obj)
        } catch (e: Exception) {
            logger.error(e) { "serialize failed: type=${obj::class.simpleName}" }
            // TODO : 매트릭 or 알림
            throw SerializationException("Serialization failed", e)
        }
    }

    /**
     * Deserialize JSON string to object.
     *
     * @throws SerializationException when JSON is invalid or mapping fails
     */
    fun <T> deserialize(json: String, clazz: Class<T>): T {
        return try {
            objectMapper.readValue(json, clazz)
        } catch (e: Exception) {
            logger.error(e) { "deserialize failed: json=$json, target=${clazz.simpleName}" }
            // TODO : 매트릭 or 알림
            throw SerializationException("Deserialization failed", e)
        }
    }
}
