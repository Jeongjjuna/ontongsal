package wemade.ontongsal.springmodulith.order.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wemade.ontongsal.springmodulith.order.application.OrderService

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        @RequestBody request: CreateOrderRequest
    ): OrderResponse {

        val command = CreateOrderCommand(
            itemId = request.itemId,
            quantity = request.quantity
        )

        val order = orderService.createOrder(command)

        return OrderResponse.from(order)
    }
}
