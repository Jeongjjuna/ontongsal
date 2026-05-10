package yjh.ontongsal.testing.application

import org.springframework.stereotype.Service
import yjh.ontongsal.testing.common.transaction.TransactionRunner
import yjh.ontongsal.testing.domain.TodoEntity
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest
import yjh.ontongsal.testing.presentation.controller.dto.TodoResponse
import yjh.ontongsal.testing.presentation.controller.dto.UpdateTodoRequest

@Service
class TodoService(
    private val transaction: TransactionRunner,
    private val todoCache: TodoCache,
    private val todoFinder: TodoFinder,
    private val todoCreator: TodoCreator,
    private val todoRemover: TodoRemover,
) {

    fun create(userId: Long, request: CreateTodoRequest): Long {
        return transaction.run {
            todoCreator.create(userId, request).id
        }
    }

    fun findById(userId: Long, todoId: Long): TodoEntity {
        return todoCache.get(todoId)
            .also { it.validateOwner(userId) }
    }

    fun findAll(userId: Long): List<TodoEntity> {
        return todoFinder.getTodos(userId)
    }

    fun update(userId: Long, todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val updatedTodo = transaction.run {
            todoFinder.getTodo(todoId)
                .also {
                    it.validateOwner(userId)
                    it.update(request.title, request.content, request.completed)
                }
        }
        return TodoResponse.from(updatedTodo)
    }

    fun delete(userId: Long, todoId: Long) {
        transaction.run {
            todoFinder.getTodo(todoId)
                .also {
                    it.validateOwner(userId)
                    todoRemover.delete(it)
                }
        }
    }
}
