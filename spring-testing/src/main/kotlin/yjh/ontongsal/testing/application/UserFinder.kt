package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.exception.AppException
import yjh.ontongsal.testing.common.exception.ErrorCode
import yjh.ontongsal.testing.domain.UserEntity
import yjh.ontongsal.testing.infrastructure.UserRepository

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {

    fun getUser(email: String): UserEntity {
        return userRepository.findByEmail(email)
            ?: throw AppException.NotFound(ErrorCode.USER_NOT_FOUND)
    }

}
