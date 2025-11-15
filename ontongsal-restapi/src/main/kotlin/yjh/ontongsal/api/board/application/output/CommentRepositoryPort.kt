package yjh.ontongsal.api.board.application.output

import yjh.ontongsal.api.board.domain.comment.Comment

interface CommentRepositoryPort {
    fun findById(commentId: Long): Comment?
    fun save(comment: Comment): Comment
}
