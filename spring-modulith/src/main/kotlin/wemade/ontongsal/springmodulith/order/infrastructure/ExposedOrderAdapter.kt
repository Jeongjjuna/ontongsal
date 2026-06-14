package wemade.ontongsal.springmodulith.order.infrastructure

import org.jetbrains.exposed.v1.jdbc.insert
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import wemade.ontongsal.springmodulith.order.application.OrderCommandPort
import wemade.ontongsal.springmodulith.order.domain.Order
import wemade.ontongsal.springmodulith.shared.transation.TransactionRunner

@Primary
@Repository
class ExposedOrderAdapter(
    private val transaction: TransactionRunner,
) : OrderCommandPort {

    override fun save(order: Order) = transaction.run {
        OrderTable.insert {
            it[id] = order.id!!
            it[itemId] = order.itemId
            it[itemName] = order.itemName
            it[price] = order.price
            it[quantity] = order.quantity
            it[status] = order.status.name
        }

        return@run order
    }
}
