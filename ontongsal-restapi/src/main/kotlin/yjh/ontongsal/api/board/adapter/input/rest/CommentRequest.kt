package yjh.ontongsal.api.board.adapter.input.rest

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import yjh.ontongsal.api.board.application.input.CommentCreateCommand
import yjh.ontongsal.api.board.application.input.CommentUpdateCommand


data class CommentCreateRequest(
    @field:NotNull(message = "댓글 내용은 필수입니다.")
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    val content: String,
)

data class CommentUpdateRequest(
    @field:NotNull(message = "댓글 내용은 필수입니다.")
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    val content: String,
)

// --------- DTO → Command 변환 ---------

fun CommentCreateRequest.toCommand(
    postId: Long,
    authorId: Long,
    authorName: String,
) = CommentCreateCommand(
    postId = postId,
    actorId = authorId,
    actorName = authorName,
    content = this.content,
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
