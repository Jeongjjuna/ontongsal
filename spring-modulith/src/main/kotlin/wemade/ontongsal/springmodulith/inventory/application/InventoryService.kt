package wemade.ontongsal.springmodulith.inventory.application

import org.springframework.stereotype.Service
import wemade.ontongsal.springmodulith.common.AppException
import wemade.ontongsal.springmodulith.common.TransactionRunner
import wemade.ontongsal.springmodulith.inventory.InventoryApi
import wemade.ontongsal.springmodulith.inventory.domain.Inventory
import wemade.ontongsal.springmodulith.inventory.domain.InventoryErrorCode

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

    override fun decreaseByItemId(itemId: String, quantity: Long): Inventory {
        if (quantity < 0) {
            throw AppException.BadRequest(InventoryErrorCode.INVALID_QUANTITY)
        }

        return transaction.run {
            val inventory = inventoryQueryPort.findByItemId(itemId)
                ?: throw AppException.NotFound(InventoryErrorCode.INVENTORY_NOT_FOUND)

            if (inventory.stock < quantity) {
                throw AppException.BadRequest(InventoryErrorCode.INSUFFICIENT_STOCK)
            }

            val updatedInventory = inventoryCommandPort.decreaseStock(itemId, quantity)

            return@run updatedInventory
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
