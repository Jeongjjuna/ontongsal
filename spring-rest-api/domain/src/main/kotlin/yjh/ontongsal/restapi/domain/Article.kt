package yjh.ontongsal.restapi.domain

import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode
import java.time.Instant

class Article(
    val id: Long = 0,
    val boardId: Long,
    val writerId: Long,
    val title: ArticleTitle,
    val content: ArticleContent,
    var deletedAt: Instant? = null,
    val auditInfo: AuditInfo,
) {

    fun update(actor: Actor, newTitle: ArticleTitle, newContent: ArticleContent) {
        if (deletedAt != null) {
            throw AppException.BadRequest(ErrorCode.ARTICLE_DELETED, "이미 삭제된 게시글입니다. 수정할 수 없습니다.")
        }
        if (!actor.isAuthor(writerId)) {
            throw AppException.Unauthorized(ErrorCode.ARTICLE_MODIFY_FORBIDDEN, "게시글 작성자만 수정할 수 있습니다.")
        }
        title.update(newTitle.contentText)
        content.update(newContent.contentText)
    }

    fun delete(actor: Actor) {
        if (!actor.isAuthor(writerId)) {
            throw AppException.Unauthorized(ErrorCode.ARTICLE_DELETE_FORBIDDEN, "게시글 작성자만 삭제할 수 있습니다.")
        }
        deletedAt = Instant.now()
    }
}
