package yjh.ontongsal.restapi.application.usecase

import yjh.ontongsal.restapi.domain.Comment

interface GetCommentUseCase {
    fun getComments(articleId: Long, commentIdCursor: Long, size: Int): List<Comment>

    fun getComment(id: Long): Comment

    fun getCommentsByArticleId(articleId: Long): List<Comment>
}
