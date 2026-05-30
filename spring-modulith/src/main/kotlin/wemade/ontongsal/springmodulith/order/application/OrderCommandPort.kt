package wemade.ontongsal.springmodulith.order.application

import wemade.ontongsal.springmodulith.order.domain.Order

interface OrderCommandPort {
    fun save(order: Order): Order
}
