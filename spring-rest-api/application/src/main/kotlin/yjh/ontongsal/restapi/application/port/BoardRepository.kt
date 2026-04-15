package yjh.ontongsal.restapi.application.port

import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Page

interface BoardRepository {
    fun save(boardName: BoardName): Board
    fun update(board: Board): Board
    fun delete(board: Board)
    fun findById(targetId: TargetId): Board?
    fun findAll(page: Int, pageSize: Int): Page<Board>
}
