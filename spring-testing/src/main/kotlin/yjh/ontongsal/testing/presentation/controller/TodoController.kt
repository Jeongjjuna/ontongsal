package yjh.ontongsal.testing.presentation.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import yjh.ontongsal.testing.application.TodoService
import yjh.ontongsal.testing.common.security.TestingUserDetails
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest
import yjh.ontongsal.testing.presentation.controller.dto.TodoResponse
import yjh.ontongsal.testing.presentation.controller.dto.UpdateTodoRequest
import yjh.ontongsal.testing.presentation.support.LocationUriBuilder

@RestController
@RequestMapping("/v1/todos")
class TodoController(
    private val todoService: TodoService,
) {
    @PostMapping
    fun create(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @RequestBody @Valid request: CreateTodoRequest,
    ): ResponseEntity<Unit> {
        val id = todoService.create(userDetails.userId, request)
        return ResponseEntity.created(LocationUriBuilder.fromCurrent(id)).build()
    }

    @GetMapping("/{id}")
    fun findById(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @PathVariable id: Long,
    ): TodoResponse = todoService.findById(userDetails.userId, id)

    @GetMapping
    fun findAll(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
    ): List<TodoResponse> = todoService.findAll(userDetails.userId)

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateTodoRequest,
    ): TodoResponse = todoService.update(userDetails.userId, id, request)

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        todoService.delete(userDetails.userId, id)
        return ResponseEntity.noContent().build()
    }
}
