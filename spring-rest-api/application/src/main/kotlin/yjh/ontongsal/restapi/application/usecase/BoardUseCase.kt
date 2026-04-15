package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Page

interface BoardUseCase {
    fun create(boardName: BoardName): Long
    fun update(boardId: TargetId, boardName: BoardName): Long
    fun delete(boardId: TargetId)

    fun findBoard(boardId: TargetId): Board
    fun findAll(page: Int, pageSize: Int): Page<Board>
}
