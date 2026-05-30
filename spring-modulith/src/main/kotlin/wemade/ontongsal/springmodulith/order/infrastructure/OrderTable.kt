package wemade.ontongsal.springmodulith.order.infrastructure

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import wemade.ontongsal.springmodulith.order.domain.Order
import wemade.ontongsal.springmodulith.order.domain.OrderStatus

object OrderTable : Table("orders") {

    val id = varchar("id", 50)
    val itemId = varchar("item_id", 50)
    val itemName = varchar("item_name", 100)
    val price = decimal("price", 10, 2)
    val quantity = long("quantity")
    val status = varchar("status", 20)

    override val primaryKey = PrimaryKey(id)
}

fun ResultRow.toOrder(): Order {
    return Order(
        id = this[OrderTable.id],
        itemId = this[OrderTable.itemId],
        itemName = this[OrderTable.itemName],
        price = this[OrderTable.price],
        quantity = this[OrderTable.quantity],
        status = OrderStatus.valueOf(this[OrderTable.status])
    )
}
