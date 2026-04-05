package yjh.ontongsal.restapi.adpater.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.stereotype.Component
import yjh.ontongsal.restapi.application.port.CircuitBreakerRunner

@Component
class CircuitBreakerAdapter(
    private val registry: CircuitBreakerRegistry,
) : CircuitBreakerRunner {

    override fun <T> execute(name: String, operation: () -> T, fallback: (Exception) -> T): T {
        val breaker = registry.circuitBreaker(name)
        return try {
            val supplier = CircuitBreaker.decorateSupplier(breaker) { operation() }
            supplier.get()
        } catch (e: Exception) {
            fallback(e)
        }
    }
}
