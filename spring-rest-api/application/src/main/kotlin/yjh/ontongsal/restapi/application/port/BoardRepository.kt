package yjh.ontongsal.restapi.application.port

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId

interface BoardRepository {
    fun save(actor: Actor, boardName: BoardName): Board
    fun update(board: Board): Board
    fun delete(board: Board)
    fun findById(targetId: TargetId): Board?
    fun findAll(): List<Board>
}
