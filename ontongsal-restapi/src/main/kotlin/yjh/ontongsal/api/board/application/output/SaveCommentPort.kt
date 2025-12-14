package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.comment.Comment

interface SaveCommentPort {
    fun create(comment: Comment): Comment

    fun update(comment: Comment): Comment
}
