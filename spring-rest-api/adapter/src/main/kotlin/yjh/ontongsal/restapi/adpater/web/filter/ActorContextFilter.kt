package yjh.ontongsal.restapi.adpater.web.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import yjh.ontongsal.restapi.adpater.context.ActorContext
import yjh.ontongsal.restapi.domain.Actor


@Component
class ActorContextFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val actor = Actor(999L, "홍길동")
            ActorContext.set(actor)

            filterChain.doFilter(request, response)
        } finally {
            ActorContext.clear()
        }
    }
}
