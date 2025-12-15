package yjh.ontongsal.api.board.application

import org.springframework.stereotype.Service
import yjh.ontongsal.api.board.application.input.GetCommentUseCase
import yjh.ontongsal.api.board.application.output.LoadCommentPort
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.common.exception.AppException
import yjh.ontongsal.api.common.exception.ErrorCode

@Service
class CommentQueryService(
    private val loadCommentPort: LoadCommentPort,
) : GetCommentUseCase {

    override fun getComment(commentId: Long): Comment {
        return loadCommentPort.findById(commentId)
            ?: throw AppException.NotFound(ErrorCode.COMMENT_NOT_FOUND, "Comment $commentId not found")
    }

    override fun getCommentsByArticleId(articleId: Long): List<Comment> {
        TODO("Not yet implemented")
    }
}
