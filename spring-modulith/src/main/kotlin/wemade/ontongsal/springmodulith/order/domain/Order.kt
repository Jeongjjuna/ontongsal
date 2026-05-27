package wemade.ontongsal.springmodulith.order.domain

import java.math.BigDecimal

class Order(
    val id: String? = null,
    val itemId: String,
    val itemName: String,
    val price: BigDecimal,
    val quantity: Long,
    var status: OrderStatus = OrderStatus.CREATED
) {

    fun confirm() {
        require(status == OrderStatus.CREATED)
        status = OrderStatus.CONFIRMED
    }

    fun cancel() {
        require(status != OrderStatus.COMPLETED)
        status = OrderStatus.CANCELLED
    }

    fun complete() {
        require(status == OrderStatus.CONFIRMED)
        status = OrderStatus.COMPLETED
    }

    companion object {

        fun create(
            itemId: String,
            itemName: String,
            price: BigDecimal,
            quantity: Long
        ): Order {

            require(quantity > 0) { "quantity must be > 0" }
            require(price >= BigDecimal.ZERO) { "price must be >= 0" }

            return Order(
                itemId = itemId,
                itemName = itemName,
                price = price,
                quantity = quantity,
                status = OrderStatus.CREATED
            )
        }
    }
}
