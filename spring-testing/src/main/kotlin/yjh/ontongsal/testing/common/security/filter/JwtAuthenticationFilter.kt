package yjh.ontongsal.testing.common.security.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import yjh.ontongsal.testing.common.security.jwt.InvalidJwtException
import yjh.ontongsal.testing.common.security.jwt.JwtTokenProvider

private val log = KotlinLogging.logger {}

/**
 * Authorization 의 jwt token 을 통해 SecurityContextHolder 를 생성해주는 필터입니다.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token: String? = resolveToken(request)

        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.setContext(context)
            } catch (e: InvalidJwtException) {
                log.debug(e) { "Invalid JWT token" }
                // 인증 실패 → 그냥 통과 (401은 EntryPoint가 처리)
            } catch (e: Exception) {
                log.debug(e) { "JWT token 검증 에러" }
                // 인증 실패 → 그냥 통과 (401은 EntryPoint가 처리)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
    }
}
