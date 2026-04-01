package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.dto.CommentWriteCommand

interface WriteCommentUseCase {
    fun write(command: CommentWriteCommand): Long
}
