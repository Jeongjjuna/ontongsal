package yjh.ontongsal.api.common.exception

import java.time.Instant
import org.springframework.http.HttpStatus


//sealed class Resource(
//    val timestamp: Instant = Instant.now(),
//    val code: Int,
//    val message: String,
//    val details: Any?,
//) {
//    @JsonPropertyOrder("timestamp", "code", "message", "details")
//    class ErrorResponse(
//        code: Int,
//        message: String,
//        details: String,
//    ) : Resource(code = code, message = message, details = details)
//
//    @JsonPropertyOrder("timestamp", "code", "message", "details")
//    class SuccessResponse(
//        message: String = HttpStatus.OK.reasonPhrase,
//        details: Any?,
//    ) : Resource(code = 0, message = message, details = details)
//}

data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val code: Int,
    val message: String,
    val details: String,
)

data class SuccessResponse(
    val code: Int = 0,
    val message: String = HttpStatus.OK.reasonPhrase,
    val details: Any?,
)
