package yjh.ontongsal.restapi.adpater.persistence.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yjh.ontongsal.restapi.adpater.persistence.jpa.entity.ArticleEntity

interface ArticleJpaRepository : JpaRepository<ArticleEntity, Long> {

    @Query("SELECT a FROM ArticleEntity a WHERE a.boardId = :boardId AND a.deletedAt IS NULL")
    fun findAllByBoardId(boardId: Long, pageable: Pageable): Slice<ArticleEntity>

    @Query("SELECT a FROM ArticleEntity a WHERE a.boardId = :boardId AND a.deletedAt IS NULL")
    fun findAllByBoardIdForPage(boardId: Long, pageable: Pageable): Page<ArticleEntity>

    @Query("SELECT a FROM ArticleEntity a WHERE a.boardId = :boardId AND a.deletedAt IS NULL ORDER BY a.id DESC")
    fun findAllByBoardIdOrderByIdDesc(boardId: Long, pageable: Pageable): Slice<ArticleEntity>

    @Query("SELECT a FROM ArticleEntity a WHERE a.boardId = :boardId AND a.deletedAt IS NULL AND a.id < :lastArticleId ORDER BY a.id DESC")
    fun findAllByBoardIdAndIdLessThanOrderByIdDesc(boardId: Long, lastArticleId: Long, pageable: Pageable): Slice<ArticleEntity>

}
