package yjh.ontongsal.testing.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.dataserializer.DataSerializer
import yjh.ontongsal.testing.common.event.Event

private val log = KotlinLogging.logger {}

@Component
class KafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    /**
     * 특정 토픽에 메세지 발행
     */
    fun publish(event: Event<*>) {
        log.info { "Sending member message: $event" }
        val data: String = DataSerializer.serialize(event)
        kafkaTemplate.send(event.type.topic, data)
    }

    /**
     * key 해시값을 기반으로 특정 파티션에 라우팅
     */
    fun publishWithKey(event: Event<*>, key: String) {
        log.info { "Sending member message with key=$key: $event" }
        val data: String = DataSerializer.serialize(event)
        kafkaTemplate.send(event.type.topic, key, data)
    }
}
