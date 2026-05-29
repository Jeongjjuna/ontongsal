package wemade.ontongsal.springmodulith.inventory.application

import org.springframework.stereotype.Service
import wemade.ontongsal.springmodulith.inventory.InventoryApi
import wemade.ontongsal.springmodulith.inventory.domain.Inventory
import wemade.ontongsal.springmodulith.inventory.domain.InventoryErrorCode
import wemade.ontongsal.springmodulith.shared.AppException
import wemade.ontongsal.springmodulith.shared.TransactionRunner

@Service
class InventoryService(
    private val transaction: TransactionRunner,
    private val inventoryCommandPort: InventoryCommandPort,
    private val inventoryQueryPort: InventoryQueryPort,
) : InventoryApi {

    fun findByItemId(itemId: String): Inventory {
        return inventoryQueryPort.findByItemId(itemId)
            ?: throw AppException.NotFound(InventoryErrorCode.INVENTORY_NOT_FOUND)
    }

    override fun decreaseByItemId(itemId: String, quantity: Long) {
        if (quantity <= 0) {
            throw AppException.BadRequest(InventoryErrorCode.INVALID_QUANTITY)
        }

        val updated = inventoryCommandPort.decreaseStock(itemId, quantity)

        if (!updated) {
            throw AppException.Conflict(InventoryErrorCode.INSUFFICIENT_STOCK)
        }
    }

    override fun updateStock(itemId: String, stock: Long): Inventory {
        if (stock < 0) {
            throw AppException.BadRequest(InventoryErrorCode.INVALID_STOCK_VALUE)
        }

        return transaction.run {
            val inventory = inventoryQueryPort.findByItemId(itemId)
                ?: throw AppException.NotFound(InventoryErrorCode.INVENTORY_NOT_FOUND)

            inventory.stock = stock

            val updatedInventory = inventoryCommandPort.save(inventory)

            return@run updatedInventory
        }
    }
}
