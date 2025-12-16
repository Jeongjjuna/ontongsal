package yjh.ontongsal.api.common.utils

import io.github.resilience4j.circuitbreaker.CircuitBreaker

object CircuitBreakerUtil {

    fun <T> CircuitBreaker.execute(
        operation: () -> T,
        fallback: (Exception) -> T,
    ): T {
        return try {
            val supplier = CircuitBreaker.decorateSupplier(this) { operation() }
            supplier.get()
        } catch (e: Exception) {
            fallback(e)
        }
    }
}
