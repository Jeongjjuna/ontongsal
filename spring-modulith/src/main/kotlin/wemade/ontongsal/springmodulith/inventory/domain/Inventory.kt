package wemade.ontongsal.springmodulith.inventory.domain

import wemade.ontongsal.springmodulith.shared.AppException

class Inventory(
    val itemId: String,
    var stock: Long,
) {

    fun decrease(quantity: Long) {
        if (quantity <= 0) {
            throw AppException.BadRequest(InventoryErrorCode.INVALID_QUANTITY)
        }

        if (stock < quantity) {
            throw AppException.BadRequest(InventoryErrorCode.INSUFFICIENT_STOCK)
        }

        stock -= quantity
    }

    fun increase(quantity: Long) {
        if (quantity <= 0) {
            throw AppException.BadRequest(InventoryErrorCode.INVALID_QUANTITY)
        }

        stock += quantity
    }
}
