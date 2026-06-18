package event.application.port

interface UserLevelReader {
    fun getUserLevel(userId: String): Int
}