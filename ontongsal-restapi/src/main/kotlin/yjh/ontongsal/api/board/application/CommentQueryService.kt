package yjh.ontongsal.api.board.application

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.stereotype.Service
import yjh.ontongsal.api.board.application.input.GetCommentUseCase
import yjh.ontongsal.api.board.application.output.LoadCommentPort
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.common.exception.AppException
import yjh.ontongsal.api.common.exception.ErrorCode
import yjh.ontongsal.api.common.utils.CircuitBreakerUtil.execute

private val logger = KotlinLogging.logger {}

@Service
class CommentQueryService(
    private val loadCommentPort: LoadCommentPort,
    private val circuitBreaker: CircuitBreakerRegistry,
) : GetCommentUseCase {

    private val breaker = circuitBreaker.circuitBreaker("comment-read")

    override fun getComments(articleId: Long, commentIdCursor: Long, size: Int): List<Comment> {
        return breaker.execute(
            operation = {
                loadCommentPort.findAllBy(articleId, commentIdCursor, size)
            },
            fallback = { e ->
                logger.error(e) { "댓글 조회 실패. articleId=$articleId, cursor=$commentIdCursor, size=$size" }
                emptyList()
            }
        )
    }

    override fun getComment(commentId: Long): Comment {
        return loadCommentPort.findById(commentId)
            ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "Comment $commentId not found")
    }

    override fun getCommentsByArticleId(articleId: Long): List<Comment> {
        TODO("Not yet implemented")
    }
}
