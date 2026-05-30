package wemade.ontongsal.springmodulith.order.infrastructure

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.order.application.OrderCommandPort
import wemade.ontongsal.springmodulith.order.domain.Order
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryOrderAdapter : OrderCommandPort {

    private val store = ConcurrentHashMap<String, Order>()

    override fun save(order: Order): Order {
        val id = order.id ?: UUID.randomUUID().toString()

        val saved = Order(
            id = id,
            itemId = order.itemId,
            itemName = order.itemName,
            price = order.price,
            quantity = order.quantity,
            status = order.status
        )

        store[id] = saved
        return saved
    }

}
