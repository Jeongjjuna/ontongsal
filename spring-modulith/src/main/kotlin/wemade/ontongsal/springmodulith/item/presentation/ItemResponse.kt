package wemade.ontongsal.springmodulith.item.presentation

import wemade.ontongsal.springmodulith.item.ItemSnapshot
import java.math.BigDecimal

data class ItemResponse(
    val itemId: String,
    val name: String,
    val price: BigDecimal
) {
    companion object {
        fun from(snapshot: ItemSnapshot): ItemResponse {
            return ItemResponse(
                itemId = snapshot.itemId,
                name = snapshot.name,
                price = snapshot.price
            )
        }
    }
}
