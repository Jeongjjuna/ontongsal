package yjh.ontongsal.testing.common.messaging.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.EventPublisher
import yjh.ontongsal.testing.common.messaging.Event
import yjh.ontongsal.testing.common.messaging.EventPayloadSerializer

private val log = KotlinLogging.logger {}

@Component
class KafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : EventPublisher {
    /**
     * 특정 토픽에 메세지 발행
     */
    override fun publish(event: Event<*>) {
        log.info { "Sending member message: $event" }
        val data: String = EventPayloadSerializer.serialize(event)
        kafkaTemplate.send(event.type.topic, data)
    }

    /**
     * key 해시값을 기반으로 특정 파티션에 라우팅
     */
    override fun publishWithKey(event: Event<*>, key: String) {
        log.info { "Sending member message with key=$key: $event" }
        val data: String = EventPayloadSerializer.serialize(event)
        kafkaTemplate.send(event.type.topic, key, data)
    }
}
