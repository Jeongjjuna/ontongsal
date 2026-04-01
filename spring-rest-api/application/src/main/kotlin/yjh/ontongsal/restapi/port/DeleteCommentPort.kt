package yjh.ontongsal.restapi.port

import yjh.ontongsal.restapi.Comment

interface DeleteCommentPort {
    fun delete(comment: Comment)
}
