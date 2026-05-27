package wemade.ontongsal.springmodulith.item.infrastructure

import org.springframework.stereotype.Component
import wemade.ontongsal.springmodulith.item.application.ItemQueryPort
import wemade.ontongsal.springmodulith.item.domain.Item
import wemade.ontongsal.springmodulith.item.domain.ItemStatus
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryItemAdapter : ItemQueryPort {

    private val store = ConcurrentHashMap<String, Item>()

    init {
        // 테스트용 초기 데이터
        store["1"] = Item(
            id = "1",
            name = "Keyboard",
            price = BigDecimal("30000"),
            status = ItemStatus.ON_SALE
        )

        store["2"] = Item(
            id = "2",
            name = "Mouse",
            price = BigDecimal("15000"),
            status = ItemStatus.SOLD_OUT
        )
    }

    override fun findById(itemId: String): Item? {
        return store[itemId]
    }
}
