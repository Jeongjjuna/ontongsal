package yjh.ontongsal.testing.common.web.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI


typealias ApiResponseEntity<T> = ResponseEntity<SuccessResponse<T>>

interface ApiController {

    fun <T> ok(
        data: T? = null,
    ): ApiResponseEntity<T> = ResponseEntity
        .ok(
            SuccessResponse(
                code = 200,
                message = HttpStatus.OK.reasonPhrase,
                data = data
            )
        )

    fun <T> created(
        locationUri: URI,
        data: T? = null,
    ): ApiResponseEntity<T> = ResponseEntity
        .created(locationUri)
        .body(
            SuccessResponse(
                code = 200,
                message = HttpStatus.CREATED.reasonPhrase,
                data = data
            )
        )
}