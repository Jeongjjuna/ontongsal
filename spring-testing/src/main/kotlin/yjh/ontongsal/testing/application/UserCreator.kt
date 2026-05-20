package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.web.security.crypto.CredentialEncoder
import yjh.ontongsal.testing.domain.User
import yjh.ontongsal.testing.infrastructure.jpa.UserJpaRepository
import yjh.ontongsal.testing.infrastructure.jpa.entity.UserJpaEntity
import yjh.ontongsal.testing.presentation.controller.dto.SignupRequest

@Component
class UserCreator(
    private val userJpaRepository: UserJpaRepository,
    private val credentialEncoder: CredentialEncoder,
) {

    fun create(request: SignupRequest): User {
        val user = User(
            email = request.email,
            password = credentialEncoder.hash(request.password),
            phone = request.phone,
            role = request.role,
        )
        return userJpaRepository.save(UserJpaEntity.fromDomain(user)).toDomain()
    }

}
