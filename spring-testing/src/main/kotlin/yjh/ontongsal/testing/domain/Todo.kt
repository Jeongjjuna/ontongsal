package yjh.ontongsal.testing.domain

import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import java.time.Instant

class Todo(
    val id: Long = 0,
    val userId: Long,
    var title: String,
    var content: String? = null,
    var completed: Boolean = false,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
) {
    fun update(title: String, content: String?, completed: Boolean) {
        this.title = title
        this.content = content
        this.completed = completed
    }

    fun validateOwner(userId: Long) {
        if (this.userId != userId) {
            throw AppException.Forbidden(ErrorCode.TODO_FORBIDDEN)
        }
    }

    companion object {
        fun create(userId: Long, title: String, content: String): Todo {
            return Todo(
                userId = userId,
                title = title,
                content = content,
                completed = false
            )
        }
    }
}
