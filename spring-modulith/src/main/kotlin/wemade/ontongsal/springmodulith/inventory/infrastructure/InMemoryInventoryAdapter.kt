package wemade.ontongsal.springmodulith.inventory.infrastructure

import org.springframework.stereotype.Repository
import wemade.ontongsal.springmodulith.inventory.application.InventoryCommandPort
import wemade.ontongsal.springmodulith.inventory.application.InventoryQueryPort
import wemade.ontongsal.springmodulith.inventory.domain.Inventory
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryInventoryAdapter : InventoryCommandPort, InventoryQueryPort {

    private val store = ConcurrentHashMap<String, Inventory>()

    init {
        val inventories = listOf(
            Inventory(
                itemId = "1",
                stock = 100
            ),
            Inventory(
                itemId = "2",
                stock = 50
            )
        )

        inventories.forEach {
            store[it.itemId] = it
        }
    }

    override fun save(inventory: Inventory): Inventory {
        store[inventory.itemId] = inventory
        return inventory
    }

    override fun findByItemId(itemId: String): Inventory? {
        return store[itemId]
    }

    override fun decreaseStock(itemId: String, quantity: Long): Inventory {
        val inventory = store[itemId]
            ?: throw IllegalStateException("Inventory not found: $itemId")

        if (quantity < 0) {
            throw IllegalArgumentException("quantity must be positive")
        }

        if (inventory.stock < quantity) {
            throw IllegalStateException("Insufficient stock")
        }

        inventory.stock -= quantity

        store[itemId] = inventory
        return inventory
    }
}
