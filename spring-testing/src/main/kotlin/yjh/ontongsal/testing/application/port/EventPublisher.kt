package yjh.ontongsal.testing.application.port

import yjh.ontongsal.testing.common.messaging.Event

interface EventPublisher {
    fun publish(event: Event<*>)
    fun publishWithKey(event: Event<*>, key: String)
}
