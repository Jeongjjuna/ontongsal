package yjh.ontongsal.restapi.publisher

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.port.EventPublisher
import yjh.ontongsal.restapi.DomainEvent

@Component
class SpringEventPublisher(
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
