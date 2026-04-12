package yjh.ontongsal.restapi.adapter.persistence.jpa.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yjh.ontongsal.restapi.adapter.persistence.jpa.entity.CommentEntity

interface CommentJpaRepository : JpaRepository<CommentEntity, Long> {

    fun findByArticleIdAndIdGreaterThanOrderByIdAsc(
        articleId: Long,
        idCursor: Long,
        pageable: Pageable,
    ): List<CommentEntity>

    @Query(
        """
        SELECT c
        FROM CommentEntity c
        WHERE c.articleId = :articleId
    """
    )
    fun findAllByArticleId(
        articleId: Long,
        pageable: Pageable,
    ): Slice<CommentEntity>

}
