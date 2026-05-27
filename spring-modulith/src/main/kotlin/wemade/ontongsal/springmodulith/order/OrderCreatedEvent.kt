package wemade.ontongsal.springmodulith.order

import java.math.BigDecimal

data class OrderCreatedEvent(
    val orderId: String,
    val itemId: String,
    val quantity: Long,
    val price: BigDecimal
)
