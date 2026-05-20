package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import yjh.ontongsal.testing.common.web.security.crypto.CredentialEncoder
import yjh.ontongsal.testing.infrastructure.UserRepository

@Component
class UserValidator(
    private val userRepository: UserRepository,
    private val credentialEncoder: CredentialEncoder,
) {

    fun validateEmailNotDuplicated(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw AppException.Conflict(ErrorCode.USER_CONFLICT)
        }
    }

    fun validatePassword(raw: String, hashed: String) {
        if (credentialEncoder.matches(raw, hashed)) {
            return
        }
        throw AppException.Unauthorized(ErrorCode.INVALID_PASSWORD)
    }
}
