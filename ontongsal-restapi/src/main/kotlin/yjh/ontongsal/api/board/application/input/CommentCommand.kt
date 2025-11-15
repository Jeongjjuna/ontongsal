package yjh.ontongsal.api.board.application.input


/**
 * 댓글 도메인 Command 모음
 */

data class CommentCreateCommand(
    val postId: Long,
    val actorId: Long,
    val actorName: String,
    val content: String,
)

data class CommentUpdateCommand(
    val id: String,
    val actorId: Long,
    val actorName: String,
    val content: String,
)

data class CommentDeleteCommand(
    val id: String,
)
