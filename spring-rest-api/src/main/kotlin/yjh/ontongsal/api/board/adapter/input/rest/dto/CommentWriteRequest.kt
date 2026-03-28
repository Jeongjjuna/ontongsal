package yjh.ontongsal.api.board.adapter.input.rest.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import yjh.ontongsal.api.board.application.input.dto.CommentWriteCommand

data class CommentCreateRequest(
    @field:NotNull(message = "댓글 내용은 필수입니다.")
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    val content: String,
)

fun CommentCreateRequest.toCommand(
    articleId: Long,
    authorId: Long,
    authorName: String,
) = CommentWriteCommand(
    articleId = articleId,
    actorId = authorId,
    actorName = authorName,
    content = this.content,
)
