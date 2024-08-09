package co.bound.exercise.bookstore.order.controller

import co.bound.exercise.bookstore.domain.event.OrderCreated
import co.bound.exercise.bookstore.order.service.CreateOrderResult
import co.bound.exercise.bookstore.order.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bookstore/order")
class OrderController(
    private val orderService: OrderService,
    private val publisher: ApplicationEventPublisher
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createOrder(
        @RequestBody
        request: CreateOrderRequest
    ): ResponseEntity<*> {
        if (request.quoteId.isBlank() || request.idempotencyId.isBlank()) {
            return ResponseEntity.badRequest().body("Request 'quoteId' and 'idempotencyId' cannot be empty")
        }
        runCatching {
            request.deliveryAddress.validate()
        }.getOrElse {
            return ResponseEntity.badRequest().body(it.message)
        }
        return when(val orderResult = orderService.createOrder(request)) {
            is CreateOrderResult.Success -> {
                val order = orderResult.order
                publisher.publishEvent(OrderCreated(this, order))
                ResponseEntity.ok(CreateOrderResponse(order.id, order.status, order.quoteId))
            }

            is CreateOrderResult.Error -> ResponseEntity.badRequest().body(orderResult.message)
        }
    }
}
