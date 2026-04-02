package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.Actor
import yjh.ontongsal.restapi.Article
import yjh.ontongsal.restapi.Comment
import yjh.ontongsal.restapi.CommentContent
import yjh.ontongsal.restapi.TargetId
import yjh.ontongsal.restapi.support.Slice

interface CommentRepository {
    fun save(actor: Actor, article: Article, commentContent: CommentContent): Comment
    fun update(comment: Comment): Comment
    fun delete(comment: Comment)

    fun findById(targetId: TargetId): Comment?
    fun findAllBy(articleId: Long, commentIdCursor: Long, size: Int): List<Comment>
    fun findAll(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
