package yjh.ontongsal.api.board.application.input

import yjh.ontongsal.api.board.application.input.dto.CommentUpdateCommand

interface UpdateCommentUseCase {
    fun update(command: CommentUpdateCommand): Long
}
