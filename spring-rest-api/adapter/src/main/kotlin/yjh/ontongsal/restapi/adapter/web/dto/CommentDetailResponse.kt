package yjh.ontongsal.restapi.adapter.web.dto

import yjh.ontongsal.restapi.domain.CommentStatus
import yjh.ontongsal.restapi.domain.Content
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
