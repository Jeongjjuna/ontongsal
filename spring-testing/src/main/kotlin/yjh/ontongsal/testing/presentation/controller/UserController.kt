package yjh.ontongsal.testing.presentation.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.testing.application.UserService
import yjh.ontongsal.testing.common.web.response.ApiController
import yjh.ontongsal.testing.common.web.response.ApiResponseEntity
import yjh.ontongsal.testing.presentation.controller.dto.LoginRequest
import yjh.ontongsal.testing.presentation.controller.dto.LoginResponse
import yjh.ontongsal.testing.presentation.controller.dto.SignupRequest
import yjh.ontongsal.testing.presentation.support.LocationUriBuilder

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
) : ApiController {

    @PostMapping
    fun signup(
        @RequestBody request: SignupRequest,
    ): ApiResponseEntity<Unit> {
        val successId = userService.signup(request)
        return created(LocationUriBuilder.fromCurrent(successId))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ApiResponseEntity<LoginResponse> {
        val token = userService.login(request)
        return ok(LoginResponse(accessToken = token))
    }
}
