package wemade.ontongsal.springmodulith.shared

import wemade.ontongsal.springmodulith.shared.messaging.Event

interface EventPublisher {
    fun publish(event: Event<*>)
    fun publishWithKey(event: Event<*>, key: String)
}
