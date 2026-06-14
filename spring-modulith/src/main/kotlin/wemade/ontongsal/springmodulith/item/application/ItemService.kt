package wemade.ontongsal.springmodulith.item.application

import org.springframework.stereotype.Service
import wemade.ontongsal.springmodulith.item.ItemApi
import wemade.ontongsal.springmodulith.item.ItemSnapshot
import wemade.ontongsal.springmodulith.item.domain.ItemErrorCode
import wemade.ontongsal.springmodulith.shared.exception.AppException

@Service
class ItemService(
    private val itemQueryPort: ItemQueryPort,
) : ItemApi {

    override fun validateOrderable(itemId: String) {
        val item = itemQueryPort.findById(itemId)
            ?: throw AppException.NotFound(ItemErrorCode.ITEM_NOT_FOUND)

        if (!item.isOrderable()) {
            throw AppException.NotFound(ItemErrorCode.ITEM_NOT_ORDERABLE)
        }
    }

    override fun getItemSnapshot(itemId: String): ItemSnapshot {
        val item = itemQueryPort.findById(itemId)
            ?: throw AppException.NotFound(ItemErrorCode.ITEM_NOT_FOUND)

        return ItemSnapshot(
            itemId = item.id,
            name = item.name,
            price = item.price
        )
    }
}
