package yjh.ontongsal.api.board.application.input

import yjh.ontongsal.api.board.domain.comment.Comment

interface GetCommentUseCase {
    fun getComment(id: Long): Comment

    fun getCommentsByArticleId(articleId: Long): List<Comment>
}
