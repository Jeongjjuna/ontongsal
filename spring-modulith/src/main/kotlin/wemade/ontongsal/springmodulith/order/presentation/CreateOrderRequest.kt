package wemade.ontongsal.springmodulith.order.presentation

data class CreateOrderRequest(
    val itemId: String,
    val quantity: Long
)
