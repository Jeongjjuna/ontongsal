package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.CommentRepository
import yjh.ontongsal.restapi.application.port.TransactionRunner
import yjh.ontongsal.restapi.domain.Actor
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

private val logger = KotlinLogging.logger { }

@Component
class CommentDeleter(
    private val commentRepository: CommentRepository,
    private val transaction: TransactionRunner,
) {

    fun delete(actor: Actor, commentId: TargetId) {
        transaction.run {
            val comment: Comment = commentRepository.findById(commentId)
                ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다. [$commentId]")
            comment.delete(actor)
            commentRepository.delete(comment)
        }.also {
            logger.info { "comment deleted : commentId=$commentId" }
        }
    }
}
