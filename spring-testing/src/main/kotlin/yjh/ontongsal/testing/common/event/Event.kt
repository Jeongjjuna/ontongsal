package yjh.ontongsal.testing.common.event

import yjh.ontongsal.testing.common.dataserializer.DataSerializer

class Event<T : EventPayload>(
    val eventId: Long, // 고유한 이벤트 발행을 나타내는 식별값(컨슈머는 이값을 기반으로 멱등성 처리를 할 수 있다)
    val type: EventType,
    val payload: T,
) {

    fun toJson(): String = DataSerializer.serialize(this)

    companion object {

        fun of(eventId: Long, type: EventType, payload: EventPayload): Event<EventPayload> {
            return Event(eventId, type, payload)
        }

        fun fromJson(json: String): Event<EventPayload> {
            val raw = DataSerializer.deserialize(json, EventRaw::class.java)
            val type = EventType.from(raw.type)
            val payload = DataSerializer.deserialize(raw.payload, type.payloadClass)
            return Event(raw.eventId, type, payload)
        }
    }

    private data class EventRaw(
        val eventId: Long,
        val type: String,
        val payload: Any,
    )
}
