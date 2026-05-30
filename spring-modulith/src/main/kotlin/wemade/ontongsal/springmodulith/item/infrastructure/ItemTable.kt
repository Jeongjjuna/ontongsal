package wemade.ontongsal.springmodulith.item.infrastructure

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import wemade.ontongsal.springmodulith.item.domain.Item
import wemade.ontongsal.springmodulith.item.domain.ItemStatus

object ItemTable : Table("items") {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val price = decimal("price", 10, 2)
    val status = varchar("status", 20)

    override val primaryKey = PrimaryKey(id)
}

fun ResultRow.toItem(): Item {
    return Item(
        id = this[ItemTable.id],
        name = this[ItemTable.name],
        price = this[ItemTable.price],
        status = ItemStatus.valueOf(this[ItemTable.status])
    )
}
