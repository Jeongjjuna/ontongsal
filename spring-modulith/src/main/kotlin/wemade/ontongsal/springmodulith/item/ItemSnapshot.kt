package wemade.ontongsal.springmodulith.item

import java.math.BigDecimal

data class ItemSnapshot(
    val itemId: String,
    val name: String,
    val price: BigDecimal
)
