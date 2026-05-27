package wemade.ontongsal.springmodulith.inventory.infrastructure

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.minus
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Primary
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import wemade.ontongsal.springmodulith.common.TransactionRunner
import wemade.ontongsal.springmodulith.inventory.application.InventoryCommandPort
import wemade.ontongsal.springmodulith.inventory.application.InventoryQueryPort
import wemade.ontongsal.springmodulith.inventory.domain.Inventory

@Primary
@Repository
class ExposedInventoryAdapter(
    private val transaction: TransactionRunner,
) : InventoryCommandPort, InventoryQueryPort {

    @Transactional
    @EventListener(ApplicationReadyEvent::class)
    fun init() {

        if (InventoryTable.selectAll().empty()) {

            InventoryTable.insert {
                it[itemId] = "1"
                it[stock] = 100
            }

            InventoryTable.insert {
                it[itemId] = "2"
                it[stock] = 150
            }
        }
    }

    override fun save(inventory: Inventory) = transaction.run {
        InventoryTable.upsert {
            it[itemId] = inventory.itemId
            it[stock] = inventory.stock
        }

        return@run inventory
    }

    override fun decreaseStock(itemId: String, quantity: Long) = transaction.run {
        val updateCount = InventoryTable.update({
            (InventoryTable.itemId eq itemId) and
                (InventoryTable.stock greaterEq quantity)
        }) {
            it[stock] = stock - quantity
            // or
            // it.update(stock, stock - 1)
        }

        return@run updateCount == 1
    }

    override fun findByItemId(itemId: String) = transaction.run {
        InventoryTable
            .selectAll()
            .where { InventoryTable.itemId eq itemId }
            .singleOrNull()
            ?.toItem()
    }
}
