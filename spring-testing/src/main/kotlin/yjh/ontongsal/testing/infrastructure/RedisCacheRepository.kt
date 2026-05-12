package yjh.ontongsal.testing.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import yjh.ontongsal.testing.common.circuitbreaker.CircuitBreakerAdapter
import yjh.ontongsal.testing.common.redis.DataSerializer
import java.time.Duration

private val log = KotlinLogging.logger {}

/**
 * 캐시 Repository 이므로, 캐시조회가 예외로 실패하더라도 null 값을 던진다.
 * 단, warn 로그 확인을 통해 개발자가 인지할 수 있도록 해야 한다.
 */
@Component
class RedisCacheRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val serializer: DataSerializer,
    private val circuitBreaker: CircuitBreakerAdapter,
) {

    fun <T> get(key: String, clazz: Class<T>): T? {
        val json: String? = try {
            circuitBreaker.run("redis") {
                redisTemplate.opsForValue().get(key)
            }
        } catch (e: Exception) {
            log.warn(e) { "Redis Cache GET failed key=$key" }
            return null
        }

        if (json == null) {
            return null
        }

        return try {
            serializer.deserialize(json, clazz)
        } catch (e: Exception) {
            log.warn(e) { "Cache deserialize failed key=$key" }
            delete(key)
            null
        }
    }

    fun set(key: String, value: Any, ttl: Duration) {
        val json = try {
            serializer.serialize(value)
        } catch (e: Exception) {
            log.warn(e) { "Cache serialize failed key=$key" }
            return
        }

        try {
            circuitBreaker.run("redis") {
                redisTemplate.opsForValue().set(key, json, ttl)
            }
        } catch (e: Exception) {
            log.warn(e) { "Redis Cache SET failed key=$key" }
            // cache write 실패는 무시 (best effort)
        }
    }

    fun delete(key: String) {
        try {
            circuitBreaker.run("redis") {
                redisTemplate.delete(key)
            }
        } catch (e: Exception) {
            log.warn(e) { "Redis Cache DELETE failed key=$key" }
            // eviction 실패도 서비스 영향 없음
        }
    }
}
