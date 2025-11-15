package yjh.ontongsal.api.board.adapter.output.publisher

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yjh.ontongsal.api.board.application.output.EventPublisher
import yjh.ontongsal.api.board.domain.DomainEvent

@Component
class BoardSpringEventPublisher(
    private val springEventPublisher: ApplicationEventPublisher,
) : EventPublisher {
    private val logger = KotlinLogging.logger {}

    override fun publish(event: DomainEvent) {
        try {
            springEventPublisher.publishEvent(event)
            logger.info { "Publishing event: $event" }
        } catch (e: Exception) {
            logger.error { "Error publishing event : ${e.message}" }
        }
    }

    override fun publishAll(events: List<DomainEvent>) {
        events.forEach { event ->
            try {
                springEventPublisher.publishEvent(event)
                logger.info { "Publishing event: $event" }
            } catch (e: Exception) {
                logger.error { "Error publishing event : ${e.message}" }
            }
        }
    }
}
