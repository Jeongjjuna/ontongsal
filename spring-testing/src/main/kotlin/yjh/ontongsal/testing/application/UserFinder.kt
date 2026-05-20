package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.UserRepository
import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import yjh.ontongsal.testing.domain.User

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {

    fun getUser(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw AppException.NotFound(ErrorCode.USER_NOT_FOUND)
    }

}
