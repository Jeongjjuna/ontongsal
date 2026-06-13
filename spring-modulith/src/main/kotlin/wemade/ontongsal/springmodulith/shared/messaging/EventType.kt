package wemade.ontongsal.springmodulith.shared.messaging

import io.github.oshai.kotlinlogging.KotlinLogging
import wemade.ontongsal.springmodulith.shared.messaging.payload.EventCompleteEventPayload

private val log = KotlinLogging.logger {}

enum class EventType(
    val payloadClass: Class<out EventPayload>,
    val topic: String,
) {
    EVENT_COMPLETED(EventCompleteEventPayload::class.java, Topic.EVENT_COMPLETED);

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
        const val EVENT_COMPLETED = "event.completed"
    }
}
