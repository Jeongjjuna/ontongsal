package yjh.ontongsal.restapi.dto

data class CommentDeleteCommand(
    val commentId: Long,
    val actorId: Long,
    val actorName: String,
)
