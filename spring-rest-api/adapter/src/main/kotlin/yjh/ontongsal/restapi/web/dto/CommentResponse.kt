package yjh.ontongsal.restapi.web.dto

import java.time.Instant
import yjh.ontongsal.restapi.Content
import yjh.ontongsal.restapi.CommentStatus

data class CommentResponse(
    val id: Long,
    val articleId: Long,
    val authorId: Long,
    val createdAt: Instant,
    var updatedAt: Instant?,
    val content: Content,
    val status: CommentStatus,
)
