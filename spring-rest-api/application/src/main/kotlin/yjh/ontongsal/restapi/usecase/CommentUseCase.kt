package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.Actor
import yjh.ontongsal.restapi.Comment
import yjh.ontongsal.restapi.CommentContent
import yjh.ontongsal.restapi.TargetId
import yjh.ontongsal.restapi.support.Slice

interface CommentUseCase {
    fun write(actor: Actor, articleId: TargetId, commentContent: CommentContent): Long
    fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Long
    fun delete(actor: Actor, commentId: TargetId)
    fun findComments(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
