package wemade.ontongsal.springmodulith.order.domain

import java.math.BigDecimal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Order(
    val id: String? = null,
    val itemId: String,
    val itemName: String,
    val price: BigDecimal,
    val quantity: Long,
    var status: OrderStatus = OrderStatus.CREATED,
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

        @OptIn(ExperimentalUuidApi::class) // kotlin 2.3 실험적 기능
        fun create(
            itemId: String,
            itemName: String,
            price: BigDecimal,
            quantity: Long,
        ): Order {

            require(quantity > 0) { "quantity must be > 0" }
            require(price >= BigDecimal.ZERO) { "price must be >= 0" }

            return Order(
                id = Uuid.generateV7().toString(),
                itemId = itemId,
                itemName = itemName,
                price = price,
                quantity = quantity,
                status = OrderStatus.CREATED
            )
        }
    }
}
