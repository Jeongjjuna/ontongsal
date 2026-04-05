package yjh.ontongsal.restapi.application.port

import yjh.ontongsal.restapi.domain.event.DomainEvent

interface EventPublisher {
    fun publish(event: DomainEvent)
    fun publishAll(events: List<DomainEvent>)
}
