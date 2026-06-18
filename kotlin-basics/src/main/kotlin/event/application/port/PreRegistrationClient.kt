package event.application.port

interface PreRegistrationClient {
    fun hasCompleted(userId: String): Boolean
}
