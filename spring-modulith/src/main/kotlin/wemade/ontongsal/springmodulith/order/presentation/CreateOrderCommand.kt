package wemade.ontongsal.springmodulith.order.presentation

data class CreateOrderCommand(
    val itemId: String,
    val quantity: Long
)
