package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.BoardRepository
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

@Component
class BoardFinder(
    private val boardRepository: BoardRepository,
) {

    fun find(boardId: TargetId): Board {
        return boardRepository.findById(boardId)
            ?: throw AppException.NotFound(ErrorCode.BOARD_NOT_FOUND, "게시판을 찾을 수 없습니다. [$boardId]")
    }

    fun findAll(): List<Board> {
        return boardRepository.findAll()
    }
}
