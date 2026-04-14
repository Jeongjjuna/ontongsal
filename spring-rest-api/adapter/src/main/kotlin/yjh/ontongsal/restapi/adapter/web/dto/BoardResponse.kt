package yjh.ontongsal.restapi.adapter.web.dto

import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardStatus
import java.time.Instant

data class BoardResponse(
    val id: Long,
    val managerId: Long,
    val name: String,
    val status: BoardStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun of(board: Board): BoardResponse {
            return BoardResponse(
                id = board.id,
                managerId = board.managerId,
                name = board.name.contentText,
                status = board.status,
                createdAt = board.auditInfo.createdAt,
                updatedAt = board.auditInfo.updatedAt,
            )
        }

        fun of(boards: List<Board>): List<BoardResponse> {
            return boards.map { of(it) }
        }
    }
}
