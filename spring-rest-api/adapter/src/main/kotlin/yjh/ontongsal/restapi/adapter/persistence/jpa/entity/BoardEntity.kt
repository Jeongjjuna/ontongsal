package yjh.ontongsal.restapi.adapter.persistence.jpa.entity

import jakarta.persistence.*
import yjh.ontongsal.restapi.domain.Board
import yjh.ontongsal.restapi.domain.BoardName
import yjh.ontongsal.restapi.domain.BoardStatus

@Entity
@Table(name = "boards")
class BoardEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    val id: Long = 0,

    @Column(nullable = false, length = 50)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: BoardStatus,

) : BaseEntity() {

    companion object {
        fun new(boardName: BoardName): BoardEntity {
            return BoardEntity(
                name = boardName.contentText,
                status = BoardStatus.ACTIVE,
            )
        }

        fun of(board: Board): BoardEntity {
            return BoardEntity(
                id = board.id,
                name = board.name.contentText,
                status = board.status,
            )
        }
    }

    fun toBoard(): Board {
        return Board(
            id = id,
            name = BoardName(name),
            status = status,
            auditInfo = this.toAuditInfo(),
        )
    }
}
