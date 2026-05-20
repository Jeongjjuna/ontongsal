package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.TodoRepository
import yjh.ontongsal.testing.domain.Todo

@Component
class TodoRemover(
    private val todoRepository: TodoRepository,
) {

    fun delete(todo: Todo) {
        todoRepository.deleteById(todo.id)
    }
}
