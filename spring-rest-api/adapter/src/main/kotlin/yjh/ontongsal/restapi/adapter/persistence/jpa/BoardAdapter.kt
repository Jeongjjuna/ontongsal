package yjh.ontongsal.restapi.adapter.persistence.jpa

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.adapter.persistence.jpa.entity.BoardEntity
import yjh.ontongsal.restapi.adapter.persistence.jpa.repository.BoardJpaRepository
import yjh.ontongsal.restapi.adapter.persistence.jpa.support.OffsetLimit
import yjh.ontongsal.restapi.application.port.BoardRepository
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Page

@Repository
class BoardAdapter(
    private val boardJpaRepository: BoardJpaRepository,
) : BoardRepository {

    override fun save(boardName: BoardName): Board {
        return boardJpaRepository.save(BoardEntity.new(boardName)).toBoard()
    }

    override fun update(board: Board): Board {
        return boardJpaRepository.save(BoardEntity.of(board)).toBoard()
    }

    override fun delete(board: Board) {
        boardJpaRepository.save(BoardEntity.of(board))
    }

    override fun findById(targetId: TargetId): Board? {
        return boardJpaRepository.findByIdOrNull(targetId.targetId)?.toBoard()
    }

    override fun findAll(page: Int, pageSize: Int): Page<Board> {
        val pageable = OffsetLimit(page, pageSize).toPageable()
        val result = boardJpaRepository.findAllBy(pageable)
        return Page(
            content = result.content.map { it.toBoard() },
            page = page / pageSize,
            size = pageSize,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            hasNext = result.hasNext(),
        )
    }
}
