package wemade.ontongsal.springmodulith.inventory.domain

import wemade.ontongsal.springmodulith.common.exception.ErrorCode

enum class InventoryErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {

    INVENTORY_NOT_FOUND(4000, "재고 정보를 찾을 수 없습니다"),

    INVALID_QUANTITY(4001, "수량은 0보다 작을 수 없습니다"),

    INSUFFICIENT_STOCK(4002, "재고가 부족합니다"),

    INVALID_STOCK_VALUE(4003, "재고 값은 0보다 작을 수 없습니다"),
}
