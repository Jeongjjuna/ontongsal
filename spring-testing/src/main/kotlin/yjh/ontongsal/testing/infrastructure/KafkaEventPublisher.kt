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
    fun publish(event: Event<*>) {
        log.info { "Sending member message: $event" }
        val data: String = DataSerializer.serialize(event)
        kafkaTemplate.send(event.type.topic, event.eventId.toString(), data)
    }

    fun publishWithKey(event: Event<*>, id: Long) {
        log.info { "Sending member message with key=$id: $event" }
        val data: String = DataSerializer.serialize(event)
        kafkaTemplate.send(event.type.topic, id.toString(), data)
    }
}
