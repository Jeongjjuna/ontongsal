package yjh.ontongsal.testing.config

import org.testcontainers.kafka.KafkaContainer

object KafkaTestContainer {

    val KAFKA_CONTAINER = KafkaContainer("apache/kafka:3.7.0")

    init {
        KAFKA_CONTAINER.start()
    }
}
