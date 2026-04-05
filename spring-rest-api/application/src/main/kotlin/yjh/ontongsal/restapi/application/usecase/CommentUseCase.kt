package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.CommentContent
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Slice

interface CommentUseCase {
    fun write(actor: Actor, articleId: TargetId, commentContent: CommentContent): Long
    fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Long
    fun delete(actor: Actor, commentId: TargetId)
    fun findComments(articleId: TargetId, offset: Int, limit: Int): Slice<Comment>
}
