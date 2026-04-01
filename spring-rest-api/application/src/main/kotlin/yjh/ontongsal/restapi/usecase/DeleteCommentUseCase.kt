package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.dto.CommentDeleteCommand

interface DeleteCommentUseCase {
    fun delete(commentDeleteCommand: CommentDeleteCommand)
}
