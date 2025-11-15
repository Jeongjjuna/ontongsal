package yjh.ontongsal.api.board.domain

class Actor(
    val id: Long,
    val userName: String,
) {
    fun isAuthor(id: Long) = this.id == id
}
