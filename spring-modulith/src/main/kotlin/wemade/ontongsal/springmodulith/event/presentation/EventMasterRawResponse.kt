package wemade.ontongsal.springmodulith.event.presentation

import wemade.ontongsal.springmodulith.event.application.port.EventMasterRow
import wemade.ontongsal.springmodulith.event.application.port.EventMasterTreeNode
import wemade.ontongsal.springmodulith.event.domain.RewardType
import wemade.ontongsal.springmodulith.event.domain.event.CompletionCondition
import wemade.ontongsal.springmodulith.event.domain.event.EventType
import java.time.LocalDateTime

// 운영툴 응답: eventMaster 한 행의 모든 컬럼.
data class EventMasterRowResponse(
    val eventId: Long,
    val parentEventId: Long,
    val type: EventType,
    val name: String,
    val isActive: Boolean,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val completionCondition: CompletionCondition?,
    val reward: RewardType?,
    val day: Int?,
) {
    companion object {
        fun from(row: EventMasterRow) = EventMasterRowResponse(
            eventId = row.eventId,
            parentEventId = row.parentEventId,
            type = row.type,
            name = row.name,
            isActive = row.isActive,
            startedAt = row.startedAt,
            endedAt = row.endedAt,
            completionCondition = row.completionCondition,
            reward = row.reward,
            day = row.day,
        )
    }
}

data class EventMasterTreeResponse(
    val parent: EventMasterRowResponse,
    val children: List<EventMasterRowResponse>,
) {
    companion object {
        fun from(node: EventMasterTreeNode) = EventMasterTreeResponse(
            parent = EventMasterRowResponse.from(node.parent),
            children = node.children.map(EventMasterRowResponse::from),
        )
    }
}
