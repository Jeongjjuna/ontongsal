package yjh.ontongsal.restapi.application.port

import yjh.ontongsal.restapi.domain.*
import yjh.ontongsal.restapi.domain.support.Slice

interface CommentRepository {
    fun save(actor: Actor, article: Article, commentContent: CommentContent): Comment
    fun update(comment: Comment): Comment
    fun delete(comment: Comment)

    fun findById(targetId: TargetId): Comment?
    fun findAllBy(articleId: Long, commentIdCursor: Long, size: Int): List<Comment>
    fun findAll(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
