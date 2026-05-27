package wemade.ontongsal.springmodulith.item.domain

import java.math.BigDecimal

class Item(
    val id: String,
    val name: String,
    val price: BigDecimal,
    private val status: ItemStatus
) {

    fun isOrderable(): Boolean {
        return status == ItemStatus.ON_SALE
    }
}
