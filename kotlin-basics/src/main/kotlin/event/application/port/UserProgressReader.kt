package event.application.port

import event.domain.UserProgress

interface UserProgressReader {
    fun getEventProgress(userId: String): UserProgress
}