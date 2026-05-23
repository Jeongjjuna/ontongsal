package yjh.ontongsal.testing.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.CacheRepository
import yjh.ontongsal.testing.application.port.TodoRepository
import yjh.ontongsal.testing.common.web.exception.AppException
import yjh.ontongsal.testing.common.web.exception.ErrorCode
import yjh.ontongsal.testing.domain.Todo
import java.time.Duration

/**
 * todo 도메인에 대한 캐시 정책을 정의 & 구현한다.
 * ex) cache-aside 패턴 적용
 *
 */
private val log = KotlinLogging.logger {}

@Component
class TodoCache(
    private val todoRepository: TodoRepository,
    private val cacheRepository: CacheRepository,
) {

    private val ttl = Duration.ofSeconds(10)

    fun get(id: Long): Todo {
        val key = cacheKey(id)

        // 1. Cache Hit
        cacheRepository.get(key, Todo::class.java)
            ?.let { return it }

        log.debug { "Cache Miss key=$key" }

        // 2. Cache Miss (or Redis 장애)
        val todo = todoRepository.findById(id)
            ?: throw AppException.NotFound(ErrorCode.TODO_NOT_FOUND)

        // 3. Cache Write (Best Effort)
        cacheRepository.set(key, todo, ttl)

        return todo
    }


    private fun cacheKey(id: Long) = "todo:$id"
}
