package wemade.ontongsal.springmodulith.event.application

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.event.application.port.EventCompletionPort
import wemade.ontongsal.springmodulith.event.domain.EventErrorCode
import wemade.ontongsal.springmodulith.event.domain.event.AttendanceEvent
import wemade.ontongsal.springmodulith.event.domain.event.AttendanceItem
import wemade.ontongsal.springmodulith.event.domain.event.CompletionCondition
import wemade.ontongsal.springmodulith.event.domain.event.Event
import wemade.ontongsal.springmodulith.event.domain.event.MissionEvent
import wemade.ontongsal.springmodulith.event.domain.event.MissionItem
import wemade.ontongsal.springmodulith.shared.exception.AppException
import java.time.LocalDate

@Component
internal class EventEvaluator(
    private val eventCompletionPort: EventCompletionPort,
) {

    // targetEventId 는 사용자가 완료하려는 자식 항목(MissionItem / AttendanceItem)의 id.
    fun checkComplete(event: Event, targetEventId: Long, userId: Long) {
        when (event) {
            is MissionEvent -> checkMissionComplete(event, targetEventId, userId)
            is AttendanceEvent -> checkAttendanceComplete(event, targetEventId, userId)
        }
    }

    private fun checkMissionComplete(
        mission: MissionEvent,
        targetEventId: Long,
        userId: Long,
    ) {
        val targetItem: MissionItem = mission.findItem(targetEventId)
            ?: throw AppException.NotFound(EventErrorCode.EVENT_NOT_FOUND)

        // 0. 이미 완료한 항목인지 (중복 참여 차단)
        ensureNotAlreadyCompleted(userId, targetEventId)

        // 1. 이전 미션을 모두 달성해야 완료 가능
        val previousIds = mission.previousItems(targetEventId).map { it.id }
        if (previousIds.isNotEmpty()) {
            val completed = eventCompletionPort.findCompletedEventIds(userId, previousIds)
            if (completed.size != previousIds.size) {
                throw AppException.BadRequest(EventErrorCode.EVENT_CONDITION_NOT_MET)
            }
        }

        // 2. 달성 조건이 CLICK 이면 그냥 성공
        // 3. 그 외 조건이면 별도 로직 (외부 API 가 될 수 도 있음)
        when (targetItem.completionCondition) {
            CompletionCondition.CLICK -> Unit
            else -> TODO("CompletionCondition=${targetItem.completionCondition} 검증 미구현 (외부 API 연동 예정)")
        }
    }

    private fun checkAttendanceComplete(
        attendance: AttendanceEvent,
        targetEventId: Long,
        userId: Long,
    ) {
        val targetItem: AttendanceItem = attendance.findItem(targetEventId)
            ?: throw AppException.NotFound(EventErrorCode.EVENT_NOT_FOUND)

        // 0. 이미 완료한 항목인지 (중복 참여 차단)
        ensureNotAlreadyCompleted(userId, targetEventId)

        // 1. 이전 출석을 모두 달성했어야 완료 가능
        val previousIds = attendance.previousItems(targetItem.day).map { it.id }
        if (previousIds.isNotEmpty()) {
            val completed = eventCompletionPort.findCompletedEventIds(userId, previousIds)
            if (completed.size != previousIds.size) {
                throw AppException.BadRequest(EventErrorCode.EVENT_CONDITION_NOT_MET)
            }
        }

        // 2. 출석하는 대상이 오늘이 맞아야 완료 가능. (이벤트 시작일 + (day-1) 일 == 오늘)
        val expectedDate = attendance.startedAt.toLocalDate().plusDays(targetItem.day.toLong() - 1)
        if (expectedDate != LocalDate.now()) {
            throw AppException.BadRequest(EventErrorCode.EVENT_CONDITION_NOT_MET)
        }
    }

    private fun ensureNotAlreadyCompleted(userId: Long, targetEventId: Long) {
        val completed = eventCompletionPort.findCompletedEventIds(userId, listOf(targetEventId))
        if (completed.isNotEmpty()) {
            throw AppException.Conflict(EventErrorCode.EVENT_ALREADY_COMPLETED)
        }
    }
}
