package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.usecase.BoardUseCase
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId

@Service
class BoardCommandService(
    private val boardCreator: BoardCreator,
    private val boardUpdater: BoardUpdater,
    private val boardDeleter: BoardDeleter,
    private val boardFinder: BoardFinder,
    private val boardEventHandler: BoardEventHandler,
) : BoardUseCase {

    override fun create(actor: Actor, boardName: BoardName): Long {
        val createdBoard = boardCreator.create(actor, boardName)
        boardEventHandler.created(actor, createdBoard)
        return createdBoard.id
    }

    override fun update(actor: Actor, boardId: TargetId, boardName: BoardName): Long {
        val updatedBoard = boardUpdater.update(actor, boardId, boardName)
        boardEventHandler.updated(actor, boardId, updatedBoard)
        return updatedBoard.id
    }

    override fun delete(actor: Actor, boardId: TargetId) {
        boardDeleter.delete(actor, boardId)
    }

    override fun findBoard(boardId: TargetId): Board {
        return boardFinder.find(boardId)
    }

    override fun findAll(): List<Board> {
        return boardFinder.findAll()
    }
}
