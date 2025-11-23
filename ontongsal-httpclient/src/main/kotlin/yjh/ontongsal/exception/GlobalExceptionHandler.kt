package yjh.ontongsal.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpClient4xxException::class)
    fun handle4xxException(ex: HttpClient4xxException): ResponseEntity<*> {
        val body = mapOf(
            "error" to "Client Error",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }

    @ExceptionHandler(HttpClient5xxException::class)
    fun handle5xxException(ex: HttpClient5xxException): ResponseEntity<*> {
        val body = mapOf(
            "error" to "Server Error",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }

    // Optional: 기타 예외 처리
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<*> {
        val body = mapOf(
            "error" to "Server Error",
            "message" to ex.message
        )
        return ResponseEntity.internalServerError().body(body)
    }
}
