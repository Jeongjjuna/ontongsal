package wemade.ontongsal.springmodulith.event.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import wemade.ontongsal.springmodulith.event.application.EventService
import wemade.ontongsal.springmodulith.event.domain.event.EventStatus

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService,
) {

    // 예: GET /events?statuses=BEFORE,ONGOING
    // 파라미터 생략 시 ONGOING 만 (기존 동작 유지).
    @GetMapping
    fun list(
        @RequestParam(required = false) statuses: List<EventStatus>?,
    ): List<EventResponse> {
        val filter = statuses?.toSet()?.takeIf { it.isNotEmpty() } ?: setOf(EventStatus.ONGOING)
        return eventService.findAllByStatuses(filter).map { EventResponse.from(it) }
    }

    @PostMapping("/{eventId}/complete")
    fun complete(
        @PathVariable eventId: Long,
        @RequestHeader("X-User-Id") userId: Long,
    ) {
        eventService.complete(eventId, userId)
    }
}
