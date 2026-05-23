package yjh.ontongsal.testing.common.web.security.jwt

data class JwtUserInfo(
    val userId: Long,
    val email: String,
    val role: String,
)
