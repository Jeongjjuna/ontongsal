package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId

interface BoardUseCase {
    fun create(actor: Actor, boardName: BoardName): Long
    fun update(actor: Actor, boardId: TargetId, boardName: BoardName): Long
    fun delete(actor: Actor, boardId: TargetId)

    fun findBoard(boardId: TargetId): Board
    fun findAll(): List<Board>
}
