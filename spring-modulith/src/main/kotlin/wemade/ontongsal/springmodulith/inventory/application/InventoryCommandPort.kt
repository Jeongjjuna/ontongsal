package wemade.ontongsal.springmodulith.inventory.application

import wemade.ontongsal.springmodulith.inventory.domain.Inventory

interface InventoryCommandPort {
    fun save(inventory: Inventory): Inventory
    fun decreaseStock(itemId: String, quantity: Long): Inventory
}
