package yjh.ontongsal.testing.domain

class User(
    val id: Long = 0,
    val email: String,
    val password: String,
    val phone: String,
    val role: UserRole,
)
