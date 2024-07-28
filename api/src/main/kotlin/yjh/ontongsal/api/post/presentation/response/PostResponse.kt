package yjh.ontongsal.api.post.presentation.response

import yjh.ontongsal.api.post.domain.Post
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var deletedAt: LocalDateTime?,
) {
    companion object {
        fun from(post: Post): PostResponse {
            return PostResponse(
                id = post.id,
                title = post.title,
                content = post.content,
                createdAt = post.createdAt!!,
                updatedAt = post.updatedAt!!,
                deletedAt = post.deletedAt
            )
        }
    }
}
