package yjh.ontongsal.restapi.domain

import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

enum class CommentStatus {
    ACTIVE,
    DELETED;

    fun delete(): CommentStatus {
        if (this == DELETED) {
            throw AppException.Conflict(ErrorCode.COMMENT_DELETED, "이미 삭제된 댓글입니다. 삭제할 수 없습닉다.")
        }
        return DELETED
    }

    fun isDeleted() = this == DELETED
}

