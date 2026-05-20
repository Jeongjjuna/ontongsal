package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.web.security.crypto.CredentialEncoder
import yjh.ontongsal.testing.domain.UserEntity
import yjh.ontongsal.testing.infrastructure.UserRepository
import yjh.ontongsal.testing.presentation.controller.dto.SignupRequest

@Component
class UserCreator(
    private val userRepository: UserRepository,
    private val credentialEncoder: CredentialEncoder,
) {

    fun create(request: SignupRequest): UserEntity {
        return UserEntity(
            email = request.email,
            password = credentialEncoder.hash(request.password),
            phone = request.phone,
            role = request.role
        ).let { userRepository.save(it) }
    }

}
