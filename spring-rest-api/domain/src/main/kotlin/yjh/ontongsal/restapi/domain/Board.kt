package yjh.ontongsal.restapi.domain

class Board(
    val id: Long = 0,
    val name: BoardName,
    var status: BoardStatus = BoardStatus.ACTIVE,
    val auditInfo: AuditInfo,
) {

    fun update(newName: BoardName) {
        name.update(newName.contentText)
    }

    fun delete() {
        status = BoardStatus.INACTIVE
    }
}
