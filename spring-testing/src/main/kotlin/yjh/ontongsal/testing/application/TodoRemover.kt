package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.domain.TodoEntity
import yjh.ontongsal.testing.infrastructure.TodoRepository

@Component
class TodoRemover(
    private val todoRepository: TodoRepository,
) {

    fun delete(todo: TodoEntity) {
        todoRepository.delete(todo)
    }
}
