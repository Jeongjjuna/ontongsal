package yjh.ontongsal.api.common.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.nio.charset.StandardCharsets
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import yjh.ontongsal.api.common.utils.mask.SensitiveMaskingUtil

private val log = KotlinLogging.logger {}

@Order(1)
@Component
class LogFilter : OncePerRequestFilter() {

    companion object {
        const val UNKNOWN = "UNKNOWN"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val reqWrapper = ContentCachingRequestWrapper(request)
        val resWrapper = ContentCachingResponseWrapper(response)

        // ❗wrapper 를 필터 체인에 넘겨야 body 가 caching 됨
        filterChain.doFilter(reqWrapper, resWrapper)

        logRequest(reqWrapper)
        logResponse(reqWrapper, resWrapper)

        // body 복원
        resWrapper.copyBodyToResponse()
    }

    /**
     * TODO : 민감 정보 로깅 제외 예정
     */
    private fun logRequest(req: ContentCachingRequestWrapper) {
        val method = req.method
        val uri = req.requestURI

        val queryParams = req.queryString ?: UNKNOWN
        val headers = getHeaders(req)

        val requestBody = String(req.contentAsByteArray, StandardCharsets.UTF_8)
            .ifBlank { UNKNOWN }

        val logMap = mapOf(
            "type" to "HTTP Request",
            "method" to method,
            "uri" to uri,
            "headers" to headers,
            "query" to queryParams,
            "body" to SensitiveMaskingUtil.maskFrom(requestBody)
        )
        log.info { logMap }
    }

    /**
     * HTTP 응답정보는 민감정보가 없을것으로 약속하고 전부 로깅한다.
     */
    private fun logResponse(req: ContentCachingRequestWrapper, res: ContentCachingResponseWrapper) {
        val status = res.status
        val method = req.method
        val uri = req.requestURI

        val responseBody = String(res.contentAsByteArray, StandardCharsets.UTF_8)
            .ifBlank { UNKNOWN }

        val logMap = mapOf(
            "type" to "HTTP Response",
            "status" to status,
            "method" to method,
            "uri" to uri,
            "body" to responseBody
        )
        log.info { logMap }
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, String> {
        val names = request.headerNames ?: return emptyMap()
        val map = mutableMapOf<String, String>()

        while (names.hasMoreElements()) {
            val name = names.nextElement()
            map[name] = request.getHeader(name)
        }

        return map
    }

}
