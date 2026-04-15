package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.EventPublisher
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.event.BoardCreatedEvent
import yjh.ontongsal.restapi.domain.event.BoardModifiedEvent

private val logger = KotlinLogging.logger { }

@Component
class BoardEventHandler(
    private val eventPublisher: EventPublisher,
) {

    fun created(board: Board) {
        eventPublisher.publish(BoardCreatedEvent.of(board))
        logger.debug { "board created event published : boardId=${board.id}" }
    }

    fun updated(boardId: TargetId, board: Board) {
        eventPublisher.publish(BoardModifiedEvent.of(boardId, board))
        logger.debug { "board modified event published : boardId=${board.id}" }
    }
}
