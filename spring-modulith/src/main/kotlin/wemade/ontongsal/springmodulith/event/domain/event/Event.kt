package wemade.ontongsal.springmodulith.event.domain.event

import java.time.LocalDateTime

sealed class Event {
    abstract val id: Long
    abstract val name: String
    abstract val isActive: Boolean
    abstract val startedAt: LocalDateTime
    abstract val endedAt: LocalDateTime
}
