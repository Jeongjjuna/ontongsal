package wemade.ontongsal.springmodulith.event.application

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.event.application.port.CreateChildEventCommand
import wemade.ontongsal.springmodulith.event.application.port.CreateParentEventCommand
import wemade.ontongsal.springmodulith.event.application.port.EventMasterTreeNode
import wemade.ontongsal.springmodulith.event.application.port.EventRepositoryPort
import wemade.ontongsal.springmodulith.event.application.port.UpdateChildEventCommand
import wemade.ontongsal.springmodulith.event.application.port.UpdateParentEventCommand
import wemade.ontongsal.springmodulith.event.domain.EventErrorCode
import wemade.ontongsal.springmodulith.event.domain.event.Event
import wemade.ontongsal.springmodulith.shared.AppException
import wemade.ontongsal.springmodulith.shared.TransactionRunner
import java.time.LocalDateTime

@Component
class EventAdminService(
    private val transaction: TransactionRunner,
    private val eventRepositoryPort: EventRepositoryPort,
) {

    fun findAll(): List<Event> = eventRepositoryPort.findAll()

    // 운영툴: DB 모든 컬럼 그대로, 부모-자식 트리로 묶어 반환.
    fun findAllMasterTrees(): List<EventMasterTreeNode> {
        val rows = eventRepositoryPort.findAllMasterRows()
        val byParent = rows.groupBy { it.parentEventId }
        val parents = byParent[0L].orEmpty().sortedBy { it.eventId }
        return parents.map { parent ->
            EventMasterTreeNode(
                parent = parent,
                children = byParent[parent.eventId].orEmpty().sortedBy { it.eventId },
            )
        }
    }

    fun createParent(command: CreateParentEventCommand): Long {
        // 새로 만드는 이벤트가 이미 시작된 시점이면 즉시 잠금 상태로 등록되는 셈 → 금지.
        if (!command.startedAt.isAfter(LocalDateTime.now())) {
            throw AppException.BadRequest(EventErrorCode.EVENT_LOCKED)
        }
        return eventRepositoryPort.createParent(command)
    }

    // 부모 + 자식 항목들을 한 트랜잭션으로 등록.
    fun createWithItems(
        parentCommand: CreateParentEventCommand,
        childCommands: List<CreateChildEventCommand>,
    ): Long {
        if (!parentCommand.startedAt.isAfter(LocalDateTime.now())) {
            throw AppException.BadRequest(EventErrorCode.EVENT_LOCKED)
        }
        return transaction.run {
            val parentId = eventRepositoryPort.createParent(parentCommand)
            childCommands.forEach { eventRepositoryPort.createChild(parentId, it) }
            parentId
        }
    }

    fun updateParent(eventId: Long, command: UpdateParentEventCommand) {
        ensureNotStarted(eventId)
        // 변경 후 startedAt 도 미래여야 함 (시작 시점을 과거로 옮길 수 없음).
        if (!command.startedAt.isAfter(LocalDateTime.now())) {
            throw AppException.BadRequest(EventErrorCode.EVENT_LOCKED)
        }
        eventRepositoryPort.updateParent(eventId, command)
    }

    fun deleteParent(eventId: Long) {
        ensureNotStarted(eventId)
        eventRepositoryPort.deleteParentCascade(eventId)
    }

    fun createChild(parentId: Long, command: CreateChildEventCommand): Long {
        ensureNotStarted(parentId)
        return eventRepositoryPort.createChild(parentId, command)
    }

    fun updateChild(childEventId: Long, command: UpdateChildEventCommand) {
        ensureNotStarted(childEventId)
        eventRepositoryPort.updateChild(childEventId, command)
    }

    fun deleteChild(childEventId: Long) {
        ensureNotStarted(childEventId)
        eventRepositoryPort.deleteChild(childEventId)
    }

    // 부모/자식 어느 id 를 넣어도 부모 트리의 startedAt 기준으로 잠금 체크.
    private fun ensureNotStarted(eventId: Long) {
        val event = eventRepositoryPort.findParentAndChildren(eventId)
            ?: throw AppException.NotFound(EventErrorCode.EVENT_NOT_FOUND)
        if (!event.startedAt.isAfter(LocalDateTime.now())) {
            throw AppException.BadRequest(EventErrorCode.EVENT_LOCKED)
        }
    }
}
