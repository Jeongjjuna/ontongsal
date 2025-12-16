package yjh.ontongsal.api.common.config.resilience4j

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CircuitBreakerConfiguration {

    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        val circuitBreakerConfiguration = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f) // 실패율 임계값 50%
            .waitDurationInOpenState(Duration.ofSeconds(30)) // Open -> Half Open 상태의 대기 시간
            .permittedNumberOfCallsInHalfOpenState(3) // Half Open 상태에서의 상태 기준
            .slidingWindowSize(5) // 최근 5개 항목만 검사
            .minimumNumberOfCalls(3)
//            .recordExceptions(
//                IOException::class.java,
//                TimeoutException::class.java
//            )
//            .ignoreExceptions(
//                AppException::class.java,
//                OtherBusinessException::class.java
//            )
            .build()
        /**
         * 최근 5번의 호출 중 최소 3번 이상 실행됐을 때,
         * 실패 비율이 50%를 넘으면 서킷을 Open 하고,
         * 30초 후 Half-Open 상태에서 3번의 호출로 회복 여부를 판단한다.
         */
        return CircuitBreakerRegistry.of(circuitBreakerConfiguration)
    }
}
