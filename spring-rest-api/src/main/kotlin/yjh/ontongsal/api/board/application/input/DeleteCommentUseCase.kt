package yjh.ontongsal.api.board.application.input

import yjh.ontongsal.api.board.application.input.dto.CommentDeleteCommand

interface DeleteCommentUseCase {
    fun delete(commentDeleteCommand: CommentDeleteCommand)
}
