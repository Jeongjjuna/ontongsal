package yjh.ontongsal.api.board.application.input.dto

data class CommentUpdateCommand(
    val id: String,
    val actorId: Long,
    val actorName: String,
    val content: String,
)
