package wemade.ontongsal.springmodulith.event.application

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.event.application.port.EventCompletionPort
import wemade.ontongsal.springmodulith.event.domain.event.Event

@Component
internal class EventCompleter(
    private val eventCompletionPort: EventCompletionPort,
) {

    // targetEventId 는 사용자가 완료한 자식 항목(MissionItem / AttendanceItem) id.
    // 반환값은 EVENT_COMPLETED 메시지 발행 시 멱등성 키(transaction_id).
    fun complete(event: Event, targetEventId: Long, userId: Long): Long {
        return eventCompletionPort.save(userId, targetEventId)
    }
}
