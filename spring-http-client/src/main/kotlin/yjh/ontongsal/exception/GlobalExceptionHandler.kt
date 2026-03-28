package yjh.ontongsal.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClientResponseException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpClient4xxException::class)
    fun handle4xxException(ex: HttpClient4xxException): ResponseEntity<*> {
        val body = mapOf(
            "error" to "외부 API 호출 에러",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }

    @ExceptionHandler(HttpClient5xxException::class)
    fun handle5xxException(ex: HttpClient5xxException): ResponseEntity<*> {
        val body = mapOf(
            "error" to "외부 API 호출 에러",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }

    @ExceptionHandler(WebClientResponseException::class)
    fun handle5xxException(ex: WebClientResponseException): ResponseEntity<*> {
        val body = mapOf(
            "error" to "외부 API 호출 에러",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }

    // Optional: 기타 예외 처리
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<*> {
        val body = mapOf(
            "error" to "Internal Server Error",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }
}
