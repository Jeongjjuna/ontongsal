package yjh.ontongsal.restapi.jpa

import org.springframework.stereotype.Repository
import yjh.ontongsal.restapi.AuditInfo
import yjh.ontongsal.restapi.Comment
import yjh.ontongsal.restapi.CommentContent
import yjh.ontongsal.restapi.CommentStatus
import yjh.ontongsal.restapi.port.DeleteCommentPort
import yjh.ontongsal.restapi.port.LoadCommentPort
import yjh.ontongsal.restapi.port.SaveCommentPort

@Repository
class CommentAdapter(
) : SaveCommentPort, DeleteCommentPort, LoadCommentPort {

    override fun create(comment: Comment): Comment {
        // TODO: JPA 연동 시 실제 저장 로직으로 교체
        return Comment(
            id = 1L,
            articleId = comment.articleId,
            authorId = comment.authorId,
            auditInfo = comment.auditInfo,
            content = comment.content,
            status = comment.status,
        )
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

    override fun findAllBy(articleId: Long, commentIdCursor: Long, size: Int): List<Comment> {
        return listOf(
            Comment(
                id = 111,
                articleId = 999,
                authorId = 111,
                auditInfo = AuditInfo(),
                content = CommentContent("샘플 댓글 내용입니다."),
                status = CommentStatus.ACTIVE
            )
        )
    }
}
