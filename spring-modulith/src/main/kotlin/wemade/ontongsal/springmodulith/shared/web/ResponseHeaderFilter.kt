package wemade.ontongsal.springmodulith.shared.web

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class ResponseHeaderFilter : OncePerRequestFilter() {

    companion object {
        const val ERROR_ORIGIN_HEADER = "X-Response-Origin"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        response.setHeader(ERROR_ORIGIN_HEADER, "self")
        filterChain.doFilter(request, response)
    }
}