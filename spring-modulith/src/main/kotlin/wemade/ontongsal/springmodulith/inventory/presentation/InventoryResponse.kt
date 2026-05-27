package wemade.ontongsal.springmodulith.inventory.presentation

import wemade.ontongsal.springmodulith.inventory.domain.Inventory

data class InventoryResponse(
    val itemId: String,
    val stock: Long
) {
    companion object {
        fun from(inventory: Inventory): InventoryResponse {
            return InventoryResponse(
                itemId = inventory.itemId,
                stock = inventory.stock
            )
        }
    }
}
