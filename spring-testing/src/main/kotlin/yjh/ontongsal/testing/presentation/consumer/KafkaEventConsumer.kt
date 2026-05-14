package yjh.ontongsal.testing.presentation.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class KafkaEventConsumer {

    @KafkaListener(
        topics = ["todo.created"],
        groupId = "todo-created-alarm-group-id",
        containerFactory = "kafkaListener"
    )
    fun consumer(
        @Header(KafkaHeaders.RECEIVED_KEY) key: String?,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        @Payload message: String,
        ack: Acknowledgment,
    ) {
        try {
            log.info {
                """
                Kafka message received
                topic=$topic
                partition=$partition
                offset=$offset
                key=$key
                payload=$message
                """.trimIndent()
            }

            // TODO business logic

            ack.acknowledge()

            log.info { "Kafka message acknowledged topic=$topic partition=$partition offset=$offset" }
        } catch (e: Exception) {

        }

    }
}
