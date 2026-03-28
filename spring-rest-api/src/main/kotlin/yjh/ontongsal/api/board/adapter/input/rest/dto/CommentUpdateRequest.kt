package yjh.ontongsal.api.board.adapter.input.rest.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import yjh.ontongsal.api.board.application.input.dto.CommentUpdateCommand

data class CommentUpdateRequest(
    @field:NotNull(message = "댓글 내용은 필수입니다.")
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    val content: String,
)

fun CommentUpdateRequest.toCommand(
    commentId: String,
    authorId: Long,
    authorName: String,
) = CommentUpdateCommand(
    id = commentId,
    actorId = authorId,
    actorName = authorName,
    content = this.content
)
