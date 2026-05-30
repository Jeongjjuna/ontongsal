package wemade.ontongsal.springmodulith.order.presentation

import wemade.ontongsal.springmodulith.order.domain.Order
import java.math.BigDecimal

data class OrderResponse(
    val orderId: String,
    val itemId: String,
    val quantity: Long,
    val price: BigDecimal,
) {
    companion object {
        fun from(order: Order): OrderResponse {
            return OrderResponse(
                orderId = order.id!!,
                itemId = order.itemId,
                quantity = order.quantity,
                price = order.price
            )
        }
    }
}
