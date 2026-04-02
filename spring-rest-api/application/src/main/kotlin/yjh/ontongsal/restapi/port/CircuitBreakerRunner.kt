package yjh.ontongsal.restapi.port

interface CircuitBreakerRunner {
    fun <T> execute(name: String, operation: () -> T, fallback: (Exception) -> T): T
}
