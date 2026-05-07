package yjh.ontongsal.testing.presentation.controller.dto

import yjh.ontongsal.testing.domain.UserRole

data class SignupRequest(
    val email: String,
    val password: String,
    val phone: String,
    val role: UserRole = UserRole.USER,
)
