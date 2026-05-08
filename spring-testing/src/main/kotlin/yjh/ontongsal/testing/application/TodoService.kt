package yjh.ontongsal.testing.application

import org.springframework.stereotype.Service
import yjh.ontongsal.testing.common.exception.AppException
import yjh.ontongsal.testing.common.exception.ErrorCode
import yjh.ontongsal.testing.common.transaction.TransactionRunner
import yjh.ontongsal.testing.domain.TodoEntity
import yjh.ontongsal.testing.infrastructure.TodoRepository
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest
import yjh.ontongsal.testing.presentation.controller.dto.TodoResponse
import yjh.ontongsal.testing.presentation.controller.dto.UpdateTodoRequest

@Service
class TodoService(
    private val transaction: TransactionRunner,
    private val todoRepository: TodoRepository,
) {

    fun create(userId: Long, request: CreateTodoRequest): Long {
        val todo = TodoEntity.create(
            userId = userId,
            title = request.title,
            content = request.content,
        )

        return transaction.run {
            todoRepository.save(todo).id
        }
    }

    fun findById(userId: Long, todoId: Long): TodoEntity {
        return todoRepository.findById(todoId)
            .orElseThrow { AppException.NotFound(ErrorCode.TODO_NOT_FOUND) }
            .also { it.validateOwner(userId) }
    }

    fun findAll(userId: Long): List<TodoEntity> {
        return todoRepository.findAllByUserId(userId)
    }

    fun update(userId: Long, todoId: Long, request: UpdateTodoRequest): TodoResponse {
        // version1. 함수형 활용
        val updatedTodo = transaction.run {
            todoRepository.findById(todoId) // 명시적 람다 return 사용 가능 return@run
                .orElseThrow { AppException.NotFound(ErrorCode.TODO_NOT_FOUND) }
                .also {
                    it.validateOwner(userId)
                    it.update(request.title, request.content, request.completed)
                }
        }

        return TodoResponse.from(updatedTodo)
    }

    fun delete(userId: Long, todoId: Long) {
        transaction.run {
            todoRepository.findById(todoId)
                .orElseThrow { AppException.NotFound(ErrorCode.TODO_NOT_FOUND) }
                .also {
                    it.validateOwner(userId)
                    todoRepository.delete(it)
                }
        }
    }
}
