package yjh.ontongsal.api.board.application.input

import yjh.ontongsal.api.board.application.input.dto.CommentWriteCommand

interface WriteCommentUseCase {
    fun write(command: CommentWriteCommand): Long
}
