package yjh.ontongsal.restapi.application

import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.usecase.CommentUseCase
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.CommentContent
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.support.Slice

@Service
class CommentCommandService(
    private val commentCreator: CommentCreator,
    private val commentUpdater: CommentUpdater,
    private val commentDeleter: CommentDeleter,
    private val commentFinder: CommentFinder,
    private val commentEventHandler: CommentEventHandler,
) : CommentUseCase {

    override fun write(actor: Actor, articleId: TargetId, commentContent: CommentContent): Long {
        val createdComment = commentCreator.create(actor, articleId, commentContent)
        commentEventHandler.created(actor, articleId, createdComment)
        return createdComment.id
    }

    override fun update(actor: Actor, commentId: TargetId, commentContent: CommentContent): Long {
        val updatedComment = commentUpdater.update(actor, commentId, commentContent)
        commentEventHandler.updated(actor, commentId, updatedComment)
        return updatedComment.id
    }

    override fun delete(actor: Actor, commentId: TargetId) {
        commentDeleter.delete(actor, commentId)
    }

    override fun findComments(
        articleId: TargetId,
        offset: Int,
        limit: Int,
    ): Slice<Comment> {
        return commentFinder.find(articleId, offset, limit)
    }
}
