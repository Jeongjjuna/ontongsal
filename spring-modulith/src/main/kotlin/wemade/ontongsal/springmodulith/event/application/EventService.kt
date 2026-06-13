package wemade.ontongsal.springmodulith.event.application

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.shared.EventPublisher
import wemade.ontongsal.springmodulith.shared.TransactionRunner
import wemade.ontongsal.springmodulith.shared.messaging.Event
import wemade.ontongsal.springmodulith.shared.messaging.EventType
import wemade.ontongsal.springmodulith.event.domain.event.Event as DomainEvent
import wemade.ontongsal.springmodulith.event.domain.event.EventStatus
import wemade.ontongsal.springmodulith.shared.messaging.payload.EventCompleteEventPayload

@Component
class EventService private constructor(
    private val transaction: TransactionRunner,
    private val eventFinder: EventFinder,
    private val eventEvaluator: EventEvaluator,
    private val eventCompleter: EventCompleter,
    private val eventPublisher: EventPublisher,
) {

    // FE 에서 유저가 참여할 수 있는 이벤트 목록 조회.
    // isActive=true 만 대상. statuses 로 시작전/진행중/종료를 조합 필터링.
    fun findAllByStatuses(statuses: Set<EventStatus>): List<DomainEvent> =
        eventFinder.findAllByStatuses(statuses)

    fun complete(eventId: Long, userId: Long) {

        val event = eventFinder.find(eventId)

        eventEvaluator.checkComplete(event, eventId, userId)

        val eventCompletedId = transaction.run {
            eventCompleter.complete(event, eventId, userId)
        }

        val eventCompleted = Event.of(
            eventCompletedId,
            EventType.EVENT_COMPLETED,
            EventCompleteEventPayload(
                eventCompleteId = eventCompletedId,
                eventId = eventId,
                userId = userId,
            )
        )
        eventPublisher.publish(eventCompleted)
    }
}
