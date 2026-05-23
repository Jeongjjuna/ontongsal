package yjh.ontongsal.testing.application.port

import yjh.ontongsal.testing.domain.Todo

interface TodoRepository {
    fun findById(id: Long): Todo?
    fun findAllByUserId(userId: Long): List<Todo>
    fun save(todo: Todo): Todo
    fun deleteById(id: Long)
}
