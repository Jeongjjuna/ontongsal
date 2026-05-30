package wemade.ontongsal.springmodulith.inventory

import wemade.ontongsal.springmodulith.inventory.domain.Inventory

/**
 * Inventory 관련된 비즈니스이므로, 조회보다는 행동하도록 요청하자.
 */
interface InventoryApi {

    fun decreaseByItemId(itemId: String, quantity: Long)

    fun updateStock(itemId: String, stock: Long): Inventory
}
