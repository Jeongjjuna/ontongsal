package yjh.ontongsal.testing.common.exception

class InvalidJwtException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
