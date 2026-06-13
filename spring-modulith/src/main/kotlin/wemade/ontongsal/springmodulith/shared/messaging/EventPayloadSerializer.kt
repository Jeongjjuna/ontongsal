package wemade.ontongsal.springmodulith.shared.messaging

import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder

object EventPayloadSerializer {

    private val objectMapper: JsonMapper = jacksonMapperBuilder()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()

    fun serialize(obj: Any): String =
        objectMapper.writeValueAsString(obj)

    fun <T> deserialize(json: String, clazz: Class<T>): T =
        objectMapper.readValue(json, clazz)

    fun <T> deserialize(data: Any, clazz: Class<T>): T =
        objectMapper.convertValue(data, clazz)
}
