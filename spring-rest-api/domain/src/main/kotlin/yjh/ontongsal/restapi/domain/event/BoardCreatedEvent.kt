package yjh.ontongsal.restapi.domain.event

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import java.time.Instant
import java.util.*

data class BoardCreatedEvent(
    val boardId: Long,
    val managerId: Long,
    val managerName: String,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent {

    companion object {
        fun of(actor: Actor, board: Board): BoardCreatedEvent {
            return BoardCreatedEvent(
                boardId = board.id,
                managerId = actor.userId,
                managerName = actor.userName,
            )
        }
    }
}
