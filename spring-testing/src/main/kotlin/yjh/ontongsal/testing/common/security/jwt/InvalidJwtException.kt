package yjh.ontongsal.testing.common.security.jwt

class InvalidJwtException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
