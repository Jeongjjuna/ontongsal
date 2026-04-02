package yjh.ontongsal.restapi.event

import java.time.Instant

interface DomainEvent {
    val occurredAt: Instant
    val eventId: String
}
