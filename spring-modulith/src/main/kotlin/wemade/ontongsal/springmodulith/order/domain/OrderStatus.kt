package wemade.ontongsal.springmodulith.order.domain

enum class OrderStatus {
    CREATED,     // 주문 생성됨 (재고/결제 전)
    CONFIRMED,   // 주문 확정 (재고 차감 완료 상태로 보는 경우 많음)
    CANCELLED,   // 주문 취소
    COMPLETED    // 주문 완료 (배송/구매 완료)
}
