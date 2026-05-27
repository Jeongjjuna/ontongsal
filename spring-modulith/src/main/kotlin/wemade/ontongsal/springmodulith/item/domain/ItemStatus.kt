package wemade.ontongsal.springmodulith.item.domain

enum class ItemStatus {
    ON_SALE,
    SOLD_OUT,
    HIDDEN,
    STOP_SELLING, // 영구 판매 종료
    DRAFT         // 관리자 임시 저장
}
