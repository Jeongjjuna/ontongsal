package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.domain.Todo
import yjh.ontongsal.testing.infrastructure.jpa.TodoJpaRepository
import yjh.ontongsal.testing.infrastructure.jpa.entity.TodoJpaEntity
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest

@Component
class TodoCreator(
    private val todoJpaRepository: TodoJpaRepository,
) {

    fun create(userId: Long, request: CreateTodoRequest): Todo {
        val todo = Todo.create(
            userId = userId,
            title = request.title,
            content = request.content,
        )
        return todoJpaRepository.save(TodoJpaEntity.fromDomain(todo)).toDomain()
    }
}
