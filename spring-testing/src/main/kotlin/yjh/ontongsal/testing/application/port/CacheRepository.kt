package yjh.ontongsal.testing.application.port

import java.time.Duration

interface CacheRepository {
    fun <T> get(key: String, clazz: Class<T>): T?
    fun set(key: String, value: Any, ttl: Duration)
    fun delete(key: String)
}
