package yjh.ontongsal.testing.presentation.controller.dto

import jakarta.validation.constraints.NotBlank

data class UpdateTodoRequest(
    @field:NotBlank
    val title: String,
    val content: String,
    val completed: Boolean,
)
