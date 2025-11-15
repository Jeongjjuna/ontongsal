package yjh.ontongsal.api.common.exception

import org.springframework.http.HttpStatus

sealed class AppException(
    open val code: Int,
    override val message: String,
    open val status: HttpStatus,
) : RuntimeException(message) {

    open class BadRequest(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, HttpStatus.BAD_REQUEST)

    open class Unauthorized(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, HttpStatus.UNAUTHORIZED)

    open class NotFound(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, HttpStatus.NOT_FOUND)

    open class Conflict(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, HttpStatus.CONFLICT)

    open class Internal(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, HttpStatus.INTERNAL_SERVER_ERROR)
}


