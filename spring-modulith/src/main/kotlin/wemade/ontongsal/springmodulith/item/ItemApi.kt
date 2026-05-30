package wemade.ontongsal.springmodulith.item

interface ItemApi {

    /**
     * 주문 가능한 상품인지 검증
     */
    fun validateOrderable(itemId: String)

    /**
     * 주문 시 필요한 상품 스냅샷 제공
     * (가격, 이름 등 - 주문 당시 고정 데이터)
     */
    fun getItemSnapshot(itemId: String): ItemSnapshot
}
