package yjh.ontongsal.api.board.adapter.output

import org.springframework.stereotype.Repository
import yjh.ontongsal.api.board.application.output.DeleteCommentPort
import yjh.ontongsal.api.board.application.output.LoadCommentPort
import yjh.ontongsal.api.board.application.output.SaveCommentPort
import yjh.ontongsal.api.board.domain.AuditInfo
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.board.domain.comment.CommentContent
import yjh.ontongsal.api.board.domain.comment.CommentStatus

@Repository
class CommentPersistenceAdapter(
) : SaveCommentPort, DeleteCommentPort, LoadCommentPort {

    override fun create(comment: Comment): Comment {
        TODO("Not yet implemented")
    }

    override fun update(comment: Comment): Comment {
        TODO("Not yet implemented")
    }

    override fun delete(comment: Comment) {
        TODO("Not yet implemented")
    }

    override fun findById(commentId: Long): Comment? {
        return Comment(
            id = 111,
            articleId = 999,
            authorId = 111,
            auditInfo = AuditInfo(),
            content = CommentContent("샘플 댓글 내용입니다."),
            status = CommentStatus.ACTIVE
        )
    }
}
