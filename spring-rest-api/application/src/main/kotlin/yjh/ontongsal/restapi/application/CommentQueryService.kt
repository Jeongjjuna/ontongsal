package yjh.ontongsal.restapi.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import yjh.ontongsal.restapi.application.port.CircuitBreakerRunner
import yjh.ontongsal.restapi.application.port.CommentRepository
import yjh.ontongsal.restapi.application.usecase.GetCommentUseCase
import yjh.ontongsal.restapi.domain.Comment
import yjh.ontongsal.restapi.domain.TargetId
import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

private val logger = KotlinLogging.logger {}

@Service
class CommentQueryService(
    private val commentRepository: CommentRepository,
    private val circuitBreakerRunner: CircuitBreakerRunner,
) : GetCommentUseCase {

    override fun getComments(articleId: Long, commentIdCursor: Long, size: Int): List<Comment> {
        return circuitBreakerRunner.execute(
            name = CircuitBreakerNames.COMMENT_READ,
            operation = {
                commentRepository.findAllBy(articleId, commentIdCursor, size)
            },
            fallback = { e ->
                logger.error(e) { "댓글 조회 실패. articleId=$articleId, cursor=$commentIdCursor, size=$size" }
                emptyList()
            }
        )
    }

    override fun getComment(commentId: Long): Comment {
        return commentRepository.findById(TargetId(commentId))
            ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "Comment $commentId not found")
    }

    override fun getCommentsByArticleId(articleId: Long): List<Comment> {
        TODO("Not yet implemented")
    }
}
