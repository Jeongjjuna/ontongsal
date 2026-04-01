package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.Comment

interface SaveCommentPort {
    fun create(comment: Comment): Comment

    fun update(comment: Comment): Comment
}
