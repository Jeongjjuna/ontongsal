package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.comment.Comment

interface DeleteCommentPort {
    fun delete(comment: Comment)
}
