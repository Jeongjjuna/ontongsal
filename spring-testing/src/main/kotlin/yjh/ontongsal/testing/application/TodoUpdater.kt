package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.TodoRepository
import yjh.ontongsal.testing.domain.Todo

@Component
class TodoUpdater(
    private val todoRepository: TodoRepository,
) {

    fun update(todo: Todo): Todo {
        return todoRepository.save(todo)
    }
}
