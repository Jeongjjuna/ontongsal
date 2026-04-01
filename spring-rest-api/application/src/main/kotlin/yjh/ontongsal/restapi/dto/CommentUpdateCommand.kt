package yjh.ontongsal.restapi.dto

data class CommentUpdateCommand(
    val id: String,
    val actorId: Long,
    val actorName: String,
    val content: String,
)
