package yjh.ontongsal.testing.controller.dto

import yjh.ontongsal.testing.domain.UserRole

data class SignupRequest(
    val email: String,
    val password: String,
    val role: UserRole = UserRole.USER,
)
