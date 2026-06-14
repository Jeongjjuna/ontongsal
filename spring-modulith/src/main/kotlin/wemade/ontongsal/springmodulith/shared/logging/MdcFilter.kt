package wemade.ontongsal.springmodulith.shared.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class MdcFilter : OncePerRequestFilter() {

    companion object {
        const val TRACE_ID = "traceId"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val traceId = UUID.randomUUID().toString()
        MDC.put(TRACE_ID, traceId)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(TRACE_ID)
        }
    }
}