package co.bound.exercise.bookstore.order.service

import co.bound.exercise.bookstore.domain.Order
import co.bound.exercise.bookstore.order.controller.CreateOrderRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class OrderRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val createdOrders = ConcurrentHashMap<String, Order>()

    fun isCreated(request: CreateOrderRequest): Boolean {
        if (createdOrders.containsKey(request.idempotencyId)) {
            logger.info("IdempotencyId: ${request.idempotencyId} already known, returning existing order")
            return true
        }
        return false
    }

    fun findByIdempotencyId(idempotencyId: String): Order? {
        val order = createdOrders[idempotencyId]
        if (order == null) {
            logger.warn("Idempotency id: $idempotencyId does not have an order, returning null")
        }
        return order
    }

    fun save(idempotencyId: String, order: Order) {
        if (createdOrders.containsKey(idempotencyId)) {
            val existingOrder = createdOrders[idempotencyId]!!
            logger.warn("Order with id:${existingOrder.id} already exists for idempotency-id: $idempotencyId")
            return
        }
        createdOrders.putIfAbsent(idempotencyId, order)
    }
}
