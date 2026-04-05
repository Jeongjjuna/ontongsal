package yjh.ontongsal.restapi.application.port

interface CircuitBreakerRunner {
    fun <T> execute(name: String, operation: () -> T, fallback: (Exception) -> T): T
}
