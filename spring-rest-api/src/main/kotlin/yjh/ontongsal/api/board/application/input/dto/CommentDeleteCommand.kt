package yjh.ontongsal.api.board.application.input.dto

data class CommentDeleteCommand(
    val commentId: Long,
    val articleId: Long,
    val actorId: Long,
    val actorName: String,
)
