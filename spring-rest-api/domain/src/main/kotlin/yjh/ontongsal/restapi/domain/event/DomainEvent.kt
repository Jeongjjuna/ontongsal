package yjh.ontongsal.restapi.domain.event

import java.time.Instant

interface DomainEvent {
    val occurredAt: Instant
    val eventId: String
}
