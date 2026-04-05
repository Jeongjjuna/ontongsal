package yjh.ontongsal.restapi.adpater.persistence.jpa

import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.adpater.persistence.jpa.entity.CommentEntity
import yjh.ontongsal.restapi.adpater.persistence.jpa.repository.CommentJpaRepository
import yjh.ontongsal.restapi.adpater.persistence.jpa.support.OffsetLimit
import yjh.ontongsal.restapi.application.port.CommentRepository
import yjh.ontongsal.restapi.domain.*
import yjh.ontongsal.restapi.domain.support.Slice

@Repository
class CommentAdapter(
    private val commentJpaRepository: CommentJpaRepository,
) : CommentRepository {

    override fun save(actor: Actor, article: Article, commentContent: CommentContent): Comment {
        val entity = CommentEntity.new(actor, article, commentContent)
        return commentJpaRepository.save(entity).toComment()
    }

    override fun update(comment: Comment): Comment {
        return commentJpaRepository.save(CommentEntity.of(comment)).toComment()
    }

    override fun delete(comment: Comment) {
        commentJpaRepository.save(CommentEntity.of(comment))
    }

    override fun findById(targetId: TargetId): Comment? {
        return commentJpaRepository.findByIdOrNull(targetId.targetId)?.toComment()
    }

    override fun findAllBy(articleId: Long, commentIdCursor: Long, size: Int): List<Comment> {
        return commentJpaRepository
            .findByArticleIdAndIdGreaterThanOrderByIdAsc(articleId, commentIdCursor, PageRequest.of(0, size))
            .map { it.toComment() }
    }

    override fun findAll(articleId: TargetId, offset: Int, limit: Int): Slice<Comment> {
        val pageable = OffsetLimit(offset, limit).toPageable()
        val result = commentJpaRepository.findAllByArticleId(articleId.targetId, pageable)

        return Slice(
            result.content.map { entity -> entity.toComment() },
            result.hasNext()
        )
    }
}
