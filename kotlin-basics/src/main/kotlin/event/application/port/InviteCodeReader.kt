package event.application.port

typealias InviterUserId = String

interface InviteCodeReader {
    fun getInvitationUser(code: String): InviterUserId?
    fun hasUsedCode(userId: String): Boolean
}