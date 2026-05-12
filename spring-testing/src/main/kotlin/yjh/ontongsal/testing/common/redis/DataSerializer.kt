package yjh.ontongsal.testing.common.redis

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class DataSerializer {

    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    fun serialize(obj: Any): String =
        objectMapper.writeValueAsString(obj)

    fun <T> deserialize(json: String, clazz: Class<T>): T =
        objectMapper.readValue(json, clazz)
}
