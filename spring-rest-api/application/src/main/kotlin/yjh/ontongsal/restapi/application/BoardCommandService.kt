package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.usecase.BoardUseCase
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Page

@Service
class BoardCommandService(
    private val boardCreator: BoardCreator,
    private val boardUpdater: BoardUpdater,
    private val boardDeleter: BoardDeleter,
    private val boardFinder: BoardFinder,
    private val boardEventHandler: BoardEventHandler,
) : BoardUseCase {

    override fun create(boardName: BoardName): Long {
        val createdBoard = boardCreator.create(boardName)
        boardEventHandler.created(createdBoard)
        return createdBoard.id
    }

    override fun update(boardId: TargetId, boardName: BoardName): Long {
        val updatedBoard = boardUpdater.update(boardId, boardName)
        boardEventHandler.updated(boardId, updatedBoard)
        return updatedBoard.id
    }

    override fun delete(boardId: TargetId) {
        boardDeleter.delete(boardId)
    }

    override fun findBoard(boardId: TargetId): Board {
        return boardFinder.find(boardId)
    }

    override fun findAll(page: Int, pageSize: Int): Page<Board> {
        return boardFinder.findAll(page, pageSize)
    }
}
