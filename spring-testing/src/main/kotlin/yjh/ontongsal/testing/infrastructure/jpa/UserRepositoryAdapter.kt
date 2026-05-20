package yjh.ontongsal.testing.infrastructure.jpa

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.UserRepository
import yjh.ontongsal.testing.domain.User
import yjh.ontongsal.testing.infrastructure.jpa.entity.UserJpaEntity

@Component
class UserRepositoryAdapter(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {

    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)?.toDomain()
    }

    override fun existsByEmail(email: String): Boolean {
        return userJpaRepository.existsByEmail(email)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(UserJpaEntity.fromDomain(user)).toDomain()
    }
}
