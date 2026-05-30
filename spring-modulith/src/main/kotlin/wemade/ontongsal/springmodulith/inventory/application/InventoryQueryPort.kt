package wemade.ontongsal.springmodulith.inventory.application

import wemade.ontongsal.springmodulith.inventory.domain.Inventory

interface InventoryQueryPort {
    fun findByItemId(itemId: String): Inventory?
}
