package wemade.ontongsal.springmodulith.shared.messaging.payload

import wemade.ontongsal.springmodulith.shared.messaging.EventPayload

data class EventCompleteEventPayload(
    val eventCompleteId: Long,
    val eventId: Long,
    val userId: Long,
) : EventPayload
