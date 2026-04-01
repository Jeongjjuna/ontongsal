package yjh.ontongsal.restapi.usecase

import yjh.ontongsal.restapi.Comment

interface GetCommentUseCase {
    fun getComments(articleId: Long, commentIdCursor: Long, size: Int): List<Comment>

    fun getComment(id: Long): Comment

    fun getCommentsByArticleId(articleId: Long): List<Comment>
}
