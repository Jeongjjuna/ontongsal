package yjh.ontongsal.testing.presentation.controller.dto

import yjh.ontongsal.testing.domain.Todo
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
        fun from(todo: Todo): TodoResponse = TodoResponse(
            id = todo.id,
            userId = todo.userId,
            title = todo.title,
            content = todo.content,
            completed = todo.completed,
            createdAt = todo.createdAt,
            updatedAt = todo.updatedAt,
        )
    }
}
