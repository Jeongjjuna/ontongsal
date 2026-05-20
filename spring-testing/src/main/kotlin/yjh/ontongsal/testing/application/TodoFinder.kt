package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import yjh.ontongsal.testing.domain.Todo
import yjh.ontongsal.testing.infrastructure.jpa.TodoJpaRepository

@Component
class TodoFinder(
    private val todoJpaRepository: TodoJpaRepository,
) {

    fun getTodo(todoId: Long): Todo {
        return todoJpaRepository.findById(todoId)
            .orElseThrow { AppException.NotFound(ErrorCode.TODO_NOT_FOUND) }
            .toDomain()
    }

    fun getTodos(userId: Long): List<Todo> {
        return todoJpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }
}
