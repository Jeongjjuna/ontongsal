package yjh.ontongsal.testing.application

import org.springframework.stereotype.Service
import yjh.ontongsal.testing.common.messaging.Event
import yjh.ontongsal.testing.common.messaging.EventType
import yjh.ontongsal.testing.common.messaging.kafka.KafkaEventPublisher
import yjh.ontongsal.testing.common.messaging.payload.TodoCreatedEventPayload
import yjh.ontongsal.testing.common.persistence.transation.TransactionRunner
import yjh.ontongsal.testing.domain.Todo
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest
import yjh.ontongsal.testing.presentation.controller.dto.TodoResponse
import yjh.ontongsal.testing.presentation.controller.dto.UpdateTodoRequest

@Service
class TodoService(
    private val transaction: TransactionRunner,
    private val kafkaEventPublisher: KafkaEventPublisher,
    private val todoCache: TodoCache,
    private val todoFinder: TodoFinder,
    private val todoCreator: TodoCreator,
    private val todoUpdater: TodoUpdater,
    private val todoRemover: TodoRemover,
) {

    fun create(userId: Long, request: CreateTodoRequest): Long {
        val todoId = transaction.run {
            todoCreator.create(userId, request).id
        }

        val event = Event.of(
            1234L,
            EventType.TODO_CREATED,
            TodoCreatedEventPayload(
                todoId = todoId,
                title = request.title,
                content = request.content,
                userId = userId,
            )
        )

        kafkaEventPublisher.publish(event)

        return todoId
    }

    fun findById(userId: Long, todoId: Long): Todo {
        return todoCache.get(todoId)
            .also { it.validateOwner(userId) }
    }

    fun findAll(userId: Long): List<Todo> {
        return todoFinder.getTodos(userId)
    }

    fun update(userId: Long, todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val updatedTodo = transaction.run {
            val todo = todoFinder.getTodo(todoId)
            todo.validateOwner(userId)
            todo.update(request.title, request.content, request.completed)
            todoUpdater.update(todo)
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
