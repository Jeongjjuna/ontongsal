package yjh.ontongsal.testing.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.testing.domain.UserEntity

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): UserEntity?
}
