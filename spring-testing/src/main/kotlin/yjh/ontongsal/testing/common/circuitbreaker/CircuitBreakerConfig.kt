package yjh.ontongsal.testing.common.circuitbreaker

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.QueryTimeoutException
import org.springframework.data.redis.RedisConnectionFailureException
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import tools.jackson.core.JacksonException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.Duration


@Configuration
class CircuitBreakerConfig {

    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        val registry = CircuitBreakerRegistry.ofDefaults()
        registry.addConfiguration("redis", redisConfig())
        registry.addConfiguration("external-api", externalApiConfig())
        registry.circuitBreaker("redis", "redis") // 서킷브레이커 이름, config 이름
        registry.circuitBreaker("external-api", "external-api")
        return registry
    }

    /**
     * 최근 5번의 호출 중 최소 3번 이상 실행됐을 때,
     * 실패 비율이 50%를 넘으면 서킷을 Open 하고,
     * 30초 후 Half-Open 상태에서 3번의 호출로 회복 여부를 판단한다.
     */
    private fun redisConfig() =
        CircuitBreakerConfig.custom()
            .failureRateThreshold(50f) // 실패율 임계값 50%
            .waitDurationInOpenState(Duration.ofSeconds(30)) // Open -> Half Open 상태로 바뀔때까지 대기 시간
            .permittedNumberOfCallsInHalfOpenState(3) // Half Open 상태에서의 상태 기준
            .slidingWindowSize(5) // 최근 5개 항목만 검사
            .minimumNumberOfCalls(3)
            .recordExceptions(
                RedisConnectionFailureException::class.java,
                QueryTimeoutException::class.java,
                IOException::class.java
            )
            .ignoreExceptions(
                JacksonException::class.java,
                IllegalArgumentException::class.java
            )
            .build()

    private fun externalApiConfig() =
        CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .waitDurationInOpenState(Duration.ofSeconds(60))
            .permittedNumberOfCallsInHalfOpenState(5)
            .slidingWindowSize(20)
            .minimumNumberOfCalls(10)
            // 외부 통신 실패만 기록
            .recordExceptions(
                IOException::class.java,
                SocketTimeoutException::class.java,
                ConnectException::class.java,
                HttpServerErrorException::class.java,
                ResourceAccessException::class.java
            )
            .ignoreExceptions(
                HttpClientErrorException.BadRequest::class.java,
                HttpClientErrorException.Unauthorized::class.java,
                IllegalArgumentException::class.java
            )
            .build()
}


