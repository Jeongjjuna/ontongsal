package yjh.ontongsal.testing.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.testing.infrastructure.jpa.entity.UserJpaEntity

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): UserJpaEntity?
}
