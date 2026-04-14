package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.BoardRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

private val logger = KotlinLogging.logger { }

@Component
class BoardUpdater(
    private val boardRepository: BoardRepository,
    private val transaction: TransactionRunner,
) {

    fun update(actor: Actor, boardId: TargetId, boardName: BoardName): Board {
        return transaction.run {
            val board: Board = boardRepository.findById(boardId)
                ?: throw AppException.NotFound(ErrorCode.BOARD_NOT_FOUND, "게시판을 찾을 수 없습니다. [$boardId]")
            board.update(actor, boardName)
            boardRepository.update(board)
        }.also {
            logger.info { "board modified : boardId=$boardId" }
        }
    }
}
