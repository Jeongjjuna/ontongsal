package wemade.ontongsal.springmodulith.order.application

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import wemade.ontongsal.springmodulith.inventory.InventoryApi
import wemade.ontongsal.springmodulith.item.ItemApi
import wemade.ontongsal.springmodulith.order.OrderCreatedEvent
import wemade.ontongsal.springmodulith.order.domain.Order
import wemade.ontongsal.springmodulith.order.presentation.CreateOrderCommand
import wemade.ontongsal.springmodulith.shared.transation.TransactionRunner

@Service
class OrderService(
    private val orderCommandPort: OrderCommandPort,

    private val itemApi: ItemApi,
    private val inventoryApi: InventoryApi,

    private val transaction: TransactionRunner,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun createOrder(command: CreateOrderCommand): Order {
        val savedOrder = transaction.run {

            // 1. 상품 주문 가능 여부 검증
            itemApi.validateOrderable(command.itemId)

            // 2. 상품 스냅샷 조회 (가격 고정)
            val itemSnapshot = itemApi.getItemSnapshot(command.itemId)

            // 3. 재고 차감
            inventoryApi.decreaseByItemId(
                itemId = command.itemId,
                quantity = command.quantity
            )

            // 4. Order 생성
            val order = Order.create(
                itemId = itemSnapshot.itemId,
                itemName = itemSnapshot.name,
                price = itemSnapshot.price,
                quantity = command.quantity
            )

            val savedOrder = orderCommandPort.save(order)

            return@run savedOrder
        }

        // 5. 이벤트 발행
        val event = OrderCreatedEvent(
            orderId = savedOrder.id!!,
            itemId = savedOrder.itemId,
            quantity = savedOrder.quantity,
            price = savedOrder.price
        )
        eventPublisher.publishEvent(event)

        return savedOrder
    }

}
