package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.exception.AppException
import yjh.ontongsal.testing.common.exception.ErrorCode
import yjh.ontongsal.testing.domain.TodoEntity
import yjh.ontongsal.testing.infrastructure.TodoRepository

@Component
class TodoFinder(
    private val todoRepository: TodoRepository,
) {

    fun getTodo(todoId: Long): TodoEntity {
        return todoRepository.findById(todoId)
            .orElseThrow { AppException.NotFound(ErrorCode.TODO_NOT_FOUND) }
    }

    fun getTodos(userId: Long): List<TodoEntity> {
        return todoRepository.findAllByUserId(userId)
    }
}
