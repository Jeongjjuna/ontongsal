package yjh.ontongsal.restapi

class Actor(
    val userId: Long,
    val userName: String,
) {
    fun isAuthor(id: Long) = this.userId == id

    companion object {
        fun system(): Actor {
            return Actor(
                userId = 0,
                userName = "SYSTEM"
            )
        }
    }
}
