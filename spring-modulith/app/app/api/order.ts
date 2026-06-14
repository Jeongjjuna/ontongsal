// OrderController — RequestMapping("/orders") 응답/요청 타입.
export type CreateOrderRequest = {
  itemId: string
  quantity: number
}

export type OrderResponse = {
  orderId: string
  itemId: string
  quantity: number
  // Jackson 기본은 BigDecimal 을 number 로 직렬화. 큰 값은 정밀도 손실 가능성 있음.
  price: number
}
