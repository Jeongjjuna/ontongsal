package yjh.ontongsal.restapi.web.dto

import yjh.ontongsal.restapi.CommentStatus
import yjh.ontongsal.restapi.Content
import java.time.Instant

data class CommentDetailResponse(
    val id: Long,
    val articleId: Long,
    val authorId: Long,
    val createdAt: Instant,
    var updatedAt: Instant?,
    val content: Content,
    val status: CommentStatus,
)
