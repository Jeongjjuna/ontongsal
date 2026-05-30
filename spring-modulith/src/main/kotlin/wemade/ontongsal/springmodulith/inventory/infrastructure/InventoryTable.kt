package wemade.ontongsal.springmodulith.inventory.infrastructure

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import wemade.ontongsal.springmodulith.inventory.domain.Inventory

object InventoryTable : Table("inventories") {
    val itemId = varchar("item_id", 50)
    val stock = long("stock")

    override val primaryKey = PrimaryKey(itemId)
}

fun ResultRow.toItem(): Inventory {
    return Inventory(
        itemId = this[InventoryTable.itemId],
        stock = this[InventoryTable.stock],
    )
}
