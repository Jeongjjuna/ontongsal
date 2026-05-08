package yjh.ontongsal.testing.presentation.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.testing.application.UserService
import yjh.ontongsal.testing.presentation.controller.dto.LoginRequest
import yjh.ontongsal.testing.presentation.controller.dto.LoginResponse
import yjh.ontongsal.testing.presentation.controller.dto.SignupRequest
import yjh.ontongsal.testing.presentation.support.LocationUriBuilder

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun signup(
        @RequestBody request: SignupRequest,
    ): ResponseEntity<Unit> {
        val successId = userService.signup(request)
        return ResponseEntity
            .created(LocationUriBuilder.fromCurrent(successId))
            .build()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val token = userService.login(request)
        return ResponseEntity.ok(LoginResponse(accessToken = token))
    }
}
