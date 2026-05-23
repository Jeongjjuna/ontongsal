package yjh.ontongsal.testing.infrastructure

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import yjh.ontongsal.testing.common.messaging.kafka.KafkaEventPublisher
import yjh.ontongsal.testing.config.IntegrationTest
import kotlin.test.Test

@DisplayName("[통합테스트] KafkaEventPublisher")
class KafkaEventPublisherTest @Autowired constructor(
    private val sut: KafkaEventPublisher,
) : IntegrationTest() {

    @Test
    fun `이벤트 발행 테스트`() {
        // TODO
    }
}
