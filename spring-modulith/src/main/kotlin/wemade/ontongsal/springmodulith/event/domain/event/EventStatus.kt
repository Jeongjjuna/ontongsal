package wemade.ontongsal.springmodulith.event.domain.event

import java.time.LocalDateTime

enum class EventStatus {
    BEFORE, ONGOING, ENDED;

    companion object {
        fun of(event: Event, now: LocalDateTime = LocalDateTime.now()): EventStatus = when {
            now.isBefore(event.startedAt) -> BEFORE
            now.isAfter(event.endedAt) -> ENDED
            else -> ONGOING
        }
    }
}
