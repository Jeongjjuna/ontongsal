package yjh.ontongsal.testing.common.circuitbreaker

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.stereotype.Component

@Component
class CircuitBreakerAdapter(
    private val registry: CircuitBreakerRegistry,
) {

    fun <T> run(
        name: String,
        operation: () -> T,
    ): T {
        val breaker = registry.circuitBreaker(name)
        return breaker.executeSupplier(operation)
    }
}
