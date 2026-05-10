package yjh.ontongsal.testing.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.testing.domain.TodoEntity

interface TodoRepository : JpaRepository<TodoEntity, Long> {
    fun findAllByUserId(userId: Long): List<TodoEntity>
}
