package yjh.ontongsal.testing.presentation.controller.dto

import yjh.ontongsal.testing.domain.TodoEntity
import java.time.Instant

data class TodoResponse(
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String?,
    val completed: Boolean,
    val createdAt: Instant?,
    val updatedAt: Instant?,
) {
    companion object {
        fun from(entity: TodoEntity): TodoResponse = TodoResponse(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            content = entity.content,
            completed = entity.completed,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }
}
