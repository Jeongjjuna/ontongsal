package wemade.ontongsal.springmodulith.item.domain

import wemade.ontongsal.springmodulith.shared.ErrorCode

enum class ItemErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {

    ITEM_NOT_FOUND(3000, "상품을 찾을 수 없습니다"),
    ITEM_NOT_ORDERABLE(3001, "해당 상품은 주문할 수 없습니다"),
}
