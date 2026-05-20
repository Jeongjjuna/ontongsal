package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import yjh.ontongsal.testing.domain.User
import yjh.ontongsal.testing.infrastructure.jpa.UserJpaRepository

@Component
class UserFinder(
    private val userJpaRepository: UserJpaRepository,
) {

    fun getUser(email: String): User {
        return userJpaRepository.findByEmail(email)?.toDomain()
            ?: throw AppException.NotFound(ErrorCode.USER_NOT_FOUND)
    }

}
