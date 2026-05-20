package yjh.ontongsal.testing.application.port

import yjh.ontongsal.testing.domain.User

interface UserRepository {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun save(user: User): User
}
