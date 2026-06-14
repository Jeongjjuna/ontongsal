package wemade.ontongsal.springmodulith.shared.messaging

interface EventPublisher {
    fun publish(event: Event<*>)
    fun publishWithKey(event: Event<*>, key: String)
}