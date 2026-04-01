package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.DomainEvent

interface EventPublisher {
    fun publish(event: DomainEvent)
    fun publishAll(events: List<DomainEvent>)
}
