package yjh.ontongsal.restapi.exception

sealed class AppException(
    open val code: Int,
    override val message: String,
    open val statusCode: Int,
) : RuntimeException(message) {

    open class BadRequest(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, 400)

    open class Unauthorized(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, 401)

    open class NotFound(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, 404)

    open class Conflict(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, 409)

    open class Internal(errorCode: ErrorCode, customMessage: String? = null) :
        AppException(errorCode.code, customMessage ?: errorCode.message, 500)
}


