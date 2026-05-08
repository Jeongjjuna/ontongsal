package yjh.ontongsal.testing.application

import org.springframework.stereotype.Service
import yjh.ontongsal.testing.common.exception.AppException
import yjh.ontongsal.testing.common.exception.ErrorCode
import yjh.ontongsal.testing.common.security.crypto.CredentialEncoder
import yjh.ontongsal.testing.common.security.jwt.JwtTokenProvider
import yjh.ontongsal.testing.common.transaction.TransactionRunner
import yjh.ontongsal.testing.domain.UserEntity
import yjh.ontongsal.testing.infrastructure.UserRepository
import yjh.ontongsal.testing.presentation.controller.dto.LoginRequest
import yjh.ontongsal.testing.presentation.controller.dto.SignupRequest
import java.time.Duration

@Service
class UserService(
    private val transaction: TransactionRunner,
    private val userRepository: UserRepository,
    private val credentialEncoder: CredentialEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    fun signup(request: SignupRequest): Long {
        return transaction.run {
            if (userRepository.existsByEmail(request.email)) {
                throw AppException.Conflict(ErrorCode.USER_CONFLICT)
            }

            UserEntity(
                email = request.email,
                password = credentialEncoder.hash(request.password),
                phone = request.phone,
                role = request.role
            ).let { userRepository.save(it).id }
        }
    }

    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw AppException.NotFound(ErrorCode.USER_NOT_FOUND)

        if (!credentialEncoder.matches(request.password, user.password)) {
            throw AppException.Unauthorized(ErrorCode.INVALID_PASSWORD)
        }

        return jwtTokenProvider.generateToken(
            userId = user.id,
            email = user.email,
            role = user.role.name,
            expiredAt = Duration.ofHours(24),
        )
    }
}
