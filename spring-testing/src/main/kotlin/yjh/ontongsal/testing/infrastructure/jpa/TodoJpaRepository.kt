package yjh.ontongsal.testing.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.testing.infrastructure.jpa.entity.TodoJpaEntity

interface TodoJpaRepository : JpaRepository<TodoJpaEntity, Long> {
    fun findAllByUserId(userId: Long): List<TodoJpaEntity>
}
