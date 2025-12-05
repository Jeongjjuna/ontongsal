package yjh.ontongsal.api.common.exception

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.lang.reflect.ParameterizedType
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class AppResponseHandler(
    private val objectMapper: ObjectMapper,
) : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>?>,
    ): Boolean {
        val genericType = returnType.genericParameterType

        // ResponseEntity<T> 가 아닌 경우 무시
        if (genericType !is ParameterizedType) {
            return false
        }

        // ResponseEntity<T> 의 T 타입
        val bodyType = genericType.actualTypeArguments.firstOrNull()

        // ErrorResponse 타입이면 감싸지 않음
        return when (bodyType) {
            is Class<*> -> bodyType != ErrorResponse::class.java
            else -> true
        }
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>?>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        val serverHttpResponse = response as ServletServerHttpResponse
        val statusCode = serverHttpResponse.servletResponse.status

        return if (body is String) {
            // StringHttpMessageConverter 가 쓰이는 경우 JSON 문자열로 변환
            objectMapper.writeValueAsString(
                SuccessResponse(
                    message = HttpStatus.valueOf(statusCode).reasonPhrase,
                    details = body
                )
            )
        } else {
            SuccessResponse(
                message = HttpStatus.valueOf(statusCode).reasonPhrase,
                details = body
            )
        }
    }

    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException): ResponseEntity<ErrorResponse> {
        logger.error { "AppException : ${ex.message}" }

        val response = ErrorResponse(
            code = ex.code,
            message = ex.status.reasonPhrase,
            details = ex.message,
        )
        return ResponseEntity.status(ex.status).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        logger.error { "Exception : ${ex.message}" }

        val response = ErrorResponse(
            code = 9999,
            message = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            details = request.getDescription(false)
//            details = "잠시 후 다시 시도해주세요.",
        )
        return ResponseEntity.status(500).body(response)
    }
}
