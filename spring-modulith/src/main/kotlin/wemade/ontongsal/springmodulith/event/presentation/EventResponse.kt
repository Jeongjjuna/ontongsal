package wemade.ontongsal.springmodulith.event.presentation

import wemade.ontongsal.springmodulith.event.domain.RewardType
import wemade.ontongsal.springmodulith.event.domain.event.AttendanceEvent
import wemade.ontongsal.springmodulith.event.domain.event.CompletionCondition
import wemade.ontongsal.springmodulith.event.domain.event.Event
import wemade.ontongsal.springmodulith.event.domain.event.EventStatus
import wemade.ontongsal.springmodulith.event.domain.event.MissionEvent
import java.time.LocalDateTime

data class EventResponse(
    val id: Long,
    val name: String,
    val type: String,
    val status: EventStatus,
    val isActive: Boolean,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val missions: List<MissionItemResponse>? = null,
    val attendances: List<AttendanceItemResponse>? = null,
) {
    companion object {
        fun from(event: Event, now: LocalDateTime = LocalDateTime.now()): EventResponse {
            val status = EventStatus.of(event, now)
            return when (event) {
                is MissionEvent -> EventResponse(
                    id = event.id,
                    name = event.name,
                    type = "MISSION",
                    status = status,
                    isActive = event.isActive,
                    startedAt = event.startedAt,
                    endedAt = event.endedAt,
                    missions = event.missions.map(MissionItemResponse::from),
                )

                is AttendanceEvent -> EventResponse(
                    id = event.id,
                    name = event.name,
                    type = "ATTENDANCE",
                    status = status,
                    isActive = event.isActive,
                    startedAt = event.startedAt,
                    endedAt = event.endedAt,
                    attendances = event.attendances.map(AttendanceItemResponse::from),
                )
            }
        }
    }
}

data class MissionItemResponse(
    val id: Long,
    val name: String,
    val completionCondition: CompletionCondition,
    val reward: RewardType,
) {
    companion object {
        fun from(item: wemade.ontongsal.springmodulith.event.domain.event.MissionItem) =
            MissionItemResponse(item.id, item.name, item.completionCondition, item.reward)
    }
}

data class AttendanceItemResponse(
    val id: Long,
    val day: Int,
    val reward: RewardType,
) {
    companion object {
        fun from(item: wemade.ontongsal.springmodulith.event.domain.event.AttendanceItem) =
            AttendanceItemResponse(item.id, item.day, item.reward)
    }
}
