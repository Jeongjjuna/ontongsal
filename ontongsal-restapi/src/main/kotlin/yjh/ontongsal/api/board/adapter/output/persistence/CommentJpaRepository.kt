package yjh.ontongsal.api.board.adapter.output.persistence

import org.springframework.stereotype.Repository
import yjh.ontongsal.api.board.application.output.CommentRepositoryPort
import yjh.ontongsal.api.board.domain.AuditInfo
import yjh.ontongsal.api.board.domain.comment.Comment
import yjh.ontongsal.api.board.domain.comment.CommentContent
import yjh.ontongsal.api.board.domain.comment.CommentStatus

@Repository
class CommentJpaRepository : CommentRepositoryPort {
    override fun save(comment: Comment): Comment {
        return comment
    }

    override fun findById(commentId: Long): Comment? {
        return Comment(
            id = 111,
            postId = 999,
            authorId = 111,
            auditInfo = AuditInfo(),
            content = CommentContent("샘플 댓글 내용입니다."),
            status = CommentStatus.ACTIVE
        )
    }
}
