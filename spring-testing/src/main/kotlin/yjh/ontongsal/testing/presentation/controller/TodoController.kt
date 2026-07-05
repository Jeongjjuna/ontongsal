package yjh.ontongsal.testing.presentation.controller

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import yjh.ontongsal.testing.application.TodoService
import yjh.ontongsal.testing.common.web.response.ApiController
import yjh.ontongsal.testing.common.web.response.ApiResponseEntity
import yjh.ontongsal.testing.common.web.security.TestingUserDetails
import yjh.ontongsal.testing.presentation.controller.dto.CreateTodoRequest
import yjh.ontongsal.testing.presentation.controller.dto.TodoResponse
import yjh.ontongsal.testing.presentation.controller.dto.UpdateTodoRequest
import yjh.ontongsal.testing.presentation.support.LocationUriBuilder

@RestController
@RequestMapping("/v1/todos")
class TodoController(
    private val todoService: TodoService,
) : ApiController {

    @PostMapping
    fun create(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @RequestBody @Valid request: CreateTodoRequest,
    ): ApiResponseEntity<Unit> {
        val id = todoService.create(userDetails.userId, request)
        return created(LocationUriBuilder.fromCurrent(id))
    }

    @GetMapping("/{id}")
    fun findById(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @PathVariable id: Long,
    ): ApiResponseEntity<TodoResponse> {
        val todo = todoService.findById(userDetails.userId, id)
        return ok(TodoResponse.from(todo))
    }

    @GetMapping
    fun findAll(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
    ): ApiResponseEntity<List<TodoResponse>> {
        val todos = todoService.findAll(userDetails.userId)
        return ok(todos.map(TodoResponse::from))
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateTodoRequest,
    ): ApiResponseEntity<TodoResponse> {
        return ok(todoService.update(userDetails.userId, id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal userDetails: TestingUserDetails,
        @PathVariable id: Long,
    ): ApiResponseEntity<Unit> {
        todoService.delete(userDetails.userId, id)
        return ok()
    }
}
