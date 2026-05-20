package yjh.ontongsal.testing.common.web.security.jwt.exception

class InvalidJwtException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
