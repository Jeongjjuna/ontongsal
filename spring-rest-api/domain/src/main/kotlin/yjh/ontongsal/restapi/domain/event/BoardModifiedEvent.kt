package yjh.ontongsal.restapi.domain.event

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.TargetId
import java.time.Instant
import java.util.*

data class BoardModifiedEvent(
    val boardId: Long,
    val managerId: Long,
    val managerName: String,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent {

    companion object {
        fun of(actor: Actor, targetId: TargetId, board: Board): BoardModifiedEvent {
            return BoardModifiedEvent(
                boardId = targetId.targetId,
                managerId = actor.userId,
                managerName = actor.userName,
            )
        }
    }
}
