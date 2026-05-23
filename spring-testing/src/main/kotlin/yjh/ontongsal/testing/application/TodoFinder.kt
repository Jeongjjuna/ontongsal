package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.TodoRepository
import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import yjh.ontongsal.testing.domain.Todo

@Component
class TodoFinder(
    private val todoRepository: TodoRepository,
) {

    fun getTodo(todoId: Long): Todo {
        return todoRepository.findById(todoId)
            ?: throw AppException.NotFound(ErrorCode.TODO_NOT_FOUND)
    }

    fun getTodos(userId: Long): List<Todo> {
        return todoRepository.findAllByUserId(userId)
    }
}
