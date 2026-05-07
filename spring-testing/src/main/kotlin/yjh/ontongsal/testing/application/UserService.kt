package yjh.ontongsal.testing.application

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.ontongsal.testing.common.exception.AppException
import yjh.ontongsal.testing.common.exception.ErrorCode
import yjh.ontongsal.testing.common.security.jwt.JwtTokenProvider
import yjh.ontongsal.testing.controller.dto.LoginRequest
import yjh.ontongsal.testing.controller.dto.SignupRequest
import yjh.ontongsal.testing.domain.UserEntity
import yjh.ontongsal.testing.infrastructure.UserRepository
import java.time.Duration

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @Transactional
    fun signup(request: SignupRequest): Long {
        if (userRepository.existsByEmail(request.email)) {
            throw AppException.Conflict(ErrorCode.USER_CONFLICT)
        }

        val userEntity = UserEntity(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = request.role
        )
        return userRepository.save(userEntity).id
    }

    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw AppException.NotFound(ErrorCode.USER_NOT_FOUND)

        if (!passwordEncoder.matches(request.password, user.password)) {
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
