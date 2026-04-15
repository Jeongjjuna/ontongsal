package yjh.ontongsal.restapi.domain.event

import yjh.ontongsal.restapi.domain.Board
import java.time.Instant
import java.util.*

data class BoardCreatedEvent(
    val boardId: Long,
    override val occurredAt: Instant = Instant.now(),
    override val eventId: String = UUID.randomUUID().toString(),
) : DomainEvent {

    companion object {
        fun of(board: Board): BoardCreatedEvent {
            return BoardCreatedEvent(boardId = board.id)
        }
    }
}
