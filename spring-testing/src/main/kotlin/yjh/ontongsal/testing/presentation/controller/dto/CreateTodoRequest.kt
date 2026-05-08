package yjh.ontongsal.testing.presentation.controller.dto

import jakarta.validation.constraints.NotBlank

data class CreateTodoRequest(
    @field:NotBlank
    val title: String,
    val content: String? = null,
)
