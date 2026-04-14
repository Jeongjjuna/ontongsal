package yjh.ontongsal.restapi.domain

import yjh.ontongsal.restapi.domain.exception.AppException
import yjh.ontongsal.restapi.domain.exception.ErrorCode

class Board(
    val id: Long = 0,
    val managerId: Long,
    val name: BoardName,
    var status: BoardStatus = BoardStatus.ACTIVE,
    val auditInfo: AuditInfo,
) {

    fun update(actor: Actor, newName: BoardName) {
        if (!actor.isAuthor(managerId)) {
            throw AppException.Unauthorized(ErrorCode.BOARD_MODIFY_FORBIDDEN, "게시판 관리자만 수정할 수 있습니다.")
        }
        name.update(newName.contentText)
    }

    fun delete(actor: Actor) {
        if (!actor.isAuthor(managerId)) {
            throw AppException.Unauthorized(ErrorCode.BOARD_DELETE_FORBIDDEN, "게시판 관리자만 삭제할 수 있습니다.")
        }
        status = BoardStatus.DELETED
    }
}
