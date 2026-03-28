package yjh.ontongsal.api.board.domain.comment

import yjh.ontongsal.api.common.exception.AppException
import yjh.ontongsal.api.common.exception.ErrorCode

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

