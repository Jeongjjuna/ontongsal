package yjh.ontongsal.testing.common.security.jwt

data class JwtUserInfo(
    val userId: Long,
    val email: String,
    val role: String,
)
