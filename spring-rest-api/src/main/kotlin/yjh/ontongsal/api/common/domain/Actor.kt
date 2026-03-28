package yjh.ontongsal.api.common.domain

class Actor(
    val id: Long,
    val userName: String,
) {
    fun isAuthor(id: Long) = this.id == id
}
