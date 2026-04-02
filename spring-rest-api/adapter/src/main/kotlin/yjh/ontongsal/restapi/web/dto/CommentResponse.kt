package yjh.ontongsal.restapi.web.dto

import yjh.ontongsal.restapi.Comment
import yjh.ontongsal.restapi.CommentStatus
import yjh.ontongsal.restapi.Content
import java.time.Instant

data class CommentResponse(
    val id: Long,
    val articleId: Long,
    val authorId: Long,
    val createdAt: Instant,
    var updatedAt: Instant?,
    val content: Content,
    val status: CommentStatus,
) {
    companion object {
        fun of(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id,
                articleId = comment.articleId,
                authorId = comment.authorId,
                createdAt = comment.auditInfo.createdAt,
                updatedAt = comment.auditInfo.updatedAt,
                content = comment.content,
                status = comment.status,
            )
        }

        fun of(comments: List<Comment>): List<CommentResponse> {
            return comments.map { of(it) }
        }
    }
}
