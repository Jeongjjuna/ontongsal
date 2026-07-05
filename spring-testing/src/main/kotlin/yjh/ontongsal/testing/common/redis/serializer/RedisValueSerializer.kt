package yjh.ontongsal.testing.common.redis.serializer

import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper

object RedisValueSerializer {

    private val objectMapper = JsonMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()

    fun serialize(obj: Any): String =
        objectMapper.writeValueAsString(obj)

    fun <T> deserialize(json: String, clazz: Class<T>): T =
        objectMapper.readValue(json, clazz)

    fun <T> deserialize(data: Any, clazz: Class<T>): T =
        objectMapper.convertValue(data, clazz)
}
