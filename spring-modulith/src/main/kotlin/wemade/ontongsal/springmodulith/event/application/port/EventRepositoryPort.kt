package wemade.ontongsal.springmodulith.event.application.port

import wemade.ontongsal.springmodulith.event.domain.RewardType
import wemade.ontongsal.springmodulith.event.domain.event.CompletionCondition
import wemade.ontongsal.springmodulith.event.domain.event.Event
import wemade.ontongsal.springmodulith.event.domain.event.EventStatus
import wemade.ontongsal.springmodulith.event.domain.event.EventType
import java.time.LocalDateTime

interface EventRepositoryPort {
    // eventId 가 부모이든 자식이든, 해당 노드가 속한 부모 트리(부모 + 모든 자식)를 반환.
    fun findParentAndChildren(eventId: Long): Event?

    // isActive=true 이며 주어진 상태(BEFORE/ONGOING/ENDED) 중 하나에 해당하는 부모 트리들.
    fun findAllByStatuses(statuses: Set<EventStatus>): List<Event>

    // admin 용: 모든 부모 이벤트 트리 반환 (활성 여부/기간 무관).
    fun findAll(): List<Event>

    // 운영툴 용: eventMaster 테이블 전 행을 DB 컬럼 그대로 반환. 부모/자식 구분 없이 평탄.
    fun findAllMasterRows(): List<EventMasterRow>

    // 변경 명령
    fun createParent(command: CreateParentEventCommand): Long
    fun createChild(parentId: Long, command: CreateChildEventCommand): Long
    fun updateParent(eventId: Long, command: UpdateParentEventCommand)
    fun updateChild(eventId: Long, command: UpdateChildEventCommand)
    fun deleteParentCascade(parentId: Long)
    fun deleteChild(eventId: Long)
}

data class CreateParentEventCommand(
    val type: EventType,
    val name: String,
    val isActive: Boolean,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
)

data class UpdateParentEventCommand(
    val name: String,
    val isActive: Boolean,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
)

data class CreateChildEventCommand(
    val name: String,
    val reward: RewardType,
    val completionCondition: CompletionCondition? = null, // 부모가 MISSION 일 때 필수
    val day: Int? = null,                                 // 부모가 ATTENDANCE 일 때 필수
)

data class UpdateChildEventCommand(
    val name: String,
    val reward: RewardType,
    val completionCondition: CompletionCondition? = null,
    val day: Int? = null,
)

// eventMaster 테이블 한 행을 그대로 표현 (필터링/가공 없음).
data class EventMasterRow(
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
)

// 운영툴 응답용: 부모 행 + 그 자식 행들.
data class EventMasterTreeNode(
    val parent: EventMasterRow,
    val children: List<EventMasterRow>,
)
