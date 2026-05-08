package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.domain.TodoEntity
import yjh.ontongsal.testing.infrastructure.TodoRepository
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest

@Component
class TodoCreator(
    private val todoRepository: TodoRepository,
) {

    fun create(userId: Long, request: CreateTodoRequest): TodoEntity {
        val todo = TodoEntity.create(
            userId = userId,
            title = request.title,
            content = request.content,
        )
        return todoRepository.save(todo)
    }
}
