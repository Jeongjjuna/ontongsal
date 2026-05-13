package yjh.ontongsal.testing.application

import org.springframework.stereotype.Service
import yjh.ontongsal.testing.common.persistence.TransactionRunner
import yjh.ontongsal.testing.common.security.jwt.JwtTokenProvider
import yjh.ontongsal.testing.presentation.controller.dto.LoginRequest
import yjh.ontongsal.testing.presentation.controller.dto.SignupRequest
import java.time.Duration

@Service
class UserService(
    private val transaction: TransactionRunner,
    private val userValidator: UserValidator,
    private val userCreator: UserCreator,
    private val userFinder: UserFinder,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    fun signup(request: SignupRequest): Long {
        return transaction.run {
            userValidator.validateEmailNotDuplicated(request.email)
            userCreator.create(request).id
        }
    }

    fun login(request: LoginRequest): String {
        val user = userFinder.getUser(email = request.email)

        userValidator.validatePassword(request.password, user.password)

        return jwtTokenProvider.generateToken(
            userId = user.id,
            email = user.email,
            role = user.role.name,
            expiredAt = Duration.ofHours(24),
        )
    }
}
