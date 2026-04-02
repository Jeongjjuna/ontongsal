package yjh.ontongsal.restapi

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.exception.AppException
import yjh.ontongsal.restapi.exception.ErrorCode
import yjh.ontongsal.restapi.port.CommentRepository
import yjh.ontongsal.restapi.port.TransactionRunner

private val logger = KotlinLogging.logger { }

@Component
class CommentUpdater(
    private val commentRepository: CommentRepository,

    private val transaction: TransactionRunner,
) {

    fun update(
        actor: Actor,
        commentId: TargetId,
        commentContent: CommentContent,
    ): Comment {
        return transaction.run {
            val comment: Comment = commentRepository.findById(commentId)
                ?: throw AppException.NotFound(ErrorCode.ARTICLE_NOT_FOUND, "댓글을 찾을 수 없습니다. [$commentId]")

            comment.update(actor, commentContent)
            commentRepository.update(comment)
        }.also {
            logger.info { "comment modified : commentId=$commentId" }
        }
    }
}
