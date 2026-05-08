package yjh.ontongsal.testing.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.ontongsal.testing.common.exception.AppException
import yjh.ontongsal.testing.common.exception.ErrorCode
import yjh.ontongsal.testing.domain.TodoEntity
import yjh.ontongsal.testing.infrastructure.TodoRepository
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest
import yjh.ontongsal.testing.presentation.controller.dto.TodoResponse
import yjh.ontongsal.testing.presentation.controller.dto.UpdateTodoRequest

@Service
class TodoService(
    private val todoRepository: TodoRepository,
) {
    @Transactional
    fun create(userId: Long, request: CreateTodoRequest): Long {
        val todo = TodoEntity(
            userId = userId,
            title = request.title,
            content = request.content,
        )
        return todoRepository.save(todo).id
    }

    fun findById(userId: Long, todoId: Long): TodoResponse {
        val todo = todoRepository.findById(todoId).orElseThrow {
            AppException.NotFound(ErrorCode.TODO_NOT_FOUND)
        }
        if (todo.userId != userId) throw AppException.Forbidden(ErrorCode.TODO_FORBIDDEN)
        return TodoResponse.from(todo)
    }

    fun findAll(userId: Long): List<TodoResponse> =
        todoRepository.findAllByUserId(userId).map(TodoResponse::from)

    @Transactional
    fun update(userId: Long, todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findById(todoId).orElseThrow {
            AppException.NotFound(ErrorCode.TODO_NOT_FOUND)
        }
        if (todo.userId != userId) throw AppException.Forbidden(ErrorCode.TODO_FORBIDDEN)
        todo.update(request.title, request.content, request.completed)
        return TodoResponse.from(todo)
    }

    @Transactional
    fun delete(userId: Long, todoId: Long) {
        val todo = todoRepository.findById(todoId).orElseThrow {
            AppException.NotFound(ErrorCode.TODO_NOT_FOUND)
        }
        if (todo.userId != userId) throw AppException.Forbidden(ErrorCode.TODO_FORBIDDEN)
        todoRepository.delete(todo)
    }
}
