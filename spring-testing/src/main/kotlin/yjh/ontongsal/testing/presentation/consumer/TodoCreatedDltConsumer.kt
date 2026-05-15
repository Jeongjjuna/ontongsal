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
class TodoCreatedDltConsumer {

    @KafkaListener(
        topics = ["todo.created.dlt"],
        groupId = "alarm-send-dlt-group-id",
        containerFactory = "kafkaListener"
    )
    fun consume(
        @Header(KafkaHeaders.RECEIVED_KEY) key: String?,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        @Payload message: String
    ) {
        log.info { "Kafka message received topic=$topic partition=$partition offset=$offset key=$key payload=$message" }

        println("실제로는 이렇게 DLT 메세지를 처리하지 않고, 수동 혹은 기타 처리 방법을 논의해서 처리해야함.")
    }
}
