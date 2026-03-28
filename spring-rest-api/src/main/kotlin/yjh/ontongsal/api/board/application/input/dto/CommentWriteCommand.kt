package yjh.ontongsal.api.board.application.input.dto

data class CommentWriteCommand(
    val articleId: Long,
    val actorId: Long,
    val actorName: String,
    val content: String,
)
