package wemade.ontongsal.springmodulith.event.application

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.event.application.port.EventRepositoryPort
import wemade.ontongsal.springmodulith.event.domain.EventErrorCode
import wemade.ontongsal.springmodulith.event.domain.event.Event
import wemade.ontongsal.springmodulith.event.domain.event.EventStatus
import wemade.ontongsal.springmodulith.shared.exception.AppException

@Component
internal class EventFinder(
    private val eventRepositoryPort: EventRepositoryPort,
) {

    fun find(eventId: Long) = eventRepositoryPort.findParentAndChildren(eventId)
        ?: throw AppException.NotFound(EventErrorCode.EVENT_NOT_FOUND)

    fun findAllByStatuses(statuses: Set<EventStatus>): List<Event> =
        eventRepositoryPort.findAllByStatuses(statuses)
}
