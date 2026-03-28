package yjh.ontongsal.api.board.domain.comment

import yjh.ontongsal.api.board.domain.AuditInfo
import yjh.ontongsal.api.board.domain.Content
import yjh.ontongsal.api.board.domain.article.Article
import yjh.ontongsal.api.common.domain.Actor
import yjh.ontongsal.api.common.exception.AppException
import yjh.ontongsal.api.common.exception.ErrorCode

class Comment(
    val id: Long = 0,
    val articleId: Long,
    val authorId: Long,
    val auditInfo: AuditInfo = AuditInfo(),
    val content: Content,
    status: CommentStatus = CommentStatus.ACTIVE,
) {
    var status: CommentStatus = status
        private set

    // 활성 댓글 여부
    fun isActive(): Boolean = status == CommentStatus.ACTIVE

    // 댓글 수정
    fun updateContent(newContent: String, actor: Actor) {
        if (status.isDeleted()) {
            throw AppException.BadRequest(ErrorCode.COMMENT_DELETED, "이미 삭제된 댓글입니다. 수정할 수 없습니다.")
        }

        if (!actor.isAuthor(authorId)) {
            throw AppException.Unauthorized(ErrorCode.COMMENT_MODIFY_FORBIDDEN, "댓글 작성자만 수정할 수 있습니다.")
        }

        content.update(newContent)
        auditInfo.update()
    }

    // 댓글 삭제 (논리 삭제)
    fun delete(actor: Actor) {
        if (!actor.isAuthor(authorId)) {
            throw AppException.Unauthorized(ErrorCode.COMMENT_DELETE_FORBIDDEN, "댓글 작성자만 삭제할 수 있습니다.")
        }

        status = status.delete()
        auditInfo.update()
    }

    companion object {
        fun create(article: Article, author: Actor, content: String): Comment {
            return Comment(
                articleId = article.id,
                authorId = author.id,
                content = CommentContent(content),
            )
        }
    }
}
