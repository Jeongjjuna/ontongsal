package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.dto.CommentUpdateCommand

interface UpdateCommentUseCase {
    fun update(command: CommentUpdateCommand): Long
}
