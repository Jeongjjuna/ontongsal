package yjh.ontongsal.api.board.adapter.input.rest.dto

import java.time.Instant
import yjh.ontongsal.api.board.domain.Content
import yjh.ontongsal.api.board.domain.comment.CommentStatus

data class CommentResponse(
    val id: Long,
    val articleId: Long,
    val authorId: Long,
    val createdAt: Instant,
    var updatedAt: Instant?,
    val content: Content,
    val status: CommentStatus,
)
