package yjh.ontongsal.api.board.application.input

import yjh.ontongsal.api.board.domain.comment.Comment

interface CommentUsecase {
    fun write(command: CommentCreateCommand): Comment
    fun read(commentId: Long): Comment
}
