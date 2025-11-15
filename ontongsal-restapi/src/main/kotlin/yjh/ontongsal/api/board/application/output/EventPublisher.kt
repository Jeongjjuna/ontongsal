package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.DomainEvent

interface EventPublisher {
    fun publish(event : DomainEvent)
    fun publishAll(events : List<DomainEvent>)
}
