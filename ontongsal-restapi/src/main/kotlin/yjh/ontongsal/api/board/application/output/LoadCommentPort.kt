package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.comment.Comment

interface LoadCommentPort {
    fun findById(commentId: Long): Comment?
}
