package yjh.ontongsal.testing.common.event

import io.github.oshai.kotlinlogging.KotlinLogging
import yjh.ontongsal.testing.common.event.payload.TodoCreatedEventPayload

private val log = KotlinLogging.logger {}

enum class EventType(
    val payloadClass: Class<out EventPayload>,
    val topic: String,
) {
    TODO_CREATED(TodoCreatedEventPayload::class.java, Topic.TODO_CREATED);

    companion object {
        fun from(type: String): EventType {
            return try {
                valueOf(type)
            } catch (e: Exception) {
                log.error(e) { "[EventType.from] type={$type}" }
                throw IllegalArgumentException("invalid event type")
            }
        }
    }

    object Topic {
        const val TODO_CREATED = "todo.created"
    }
}
