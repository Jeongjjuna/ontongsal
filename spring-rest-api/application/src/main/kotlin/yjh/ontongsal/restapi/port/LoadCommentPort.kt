package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.Comment

interface LoadCommentPort {
    fun findById(commentId: Long): Comment?
    fun findAllBy(articleId: Long, commentIdCursor: Long, size: Int): List<Comment>
}
