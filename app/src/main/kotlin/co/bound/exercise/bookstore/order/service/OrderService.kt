package co.bound.exercise.bookstore.order.service

import co.bound.exercise.bookstore.order.controller.CreateOrderRequest
import co.bound.exercise.bookstore.domain.Order

interface OrderService {

    fun createOrder(request: CreateOrderRequest): CreateOrderResult
}

sealed interface CreateOrderResult {

    data class Success(val order: Order) : CreateOrderResult
    data class Error(private val quoteId: String): CreateOrderResult {
        val message get() = "Error creating order for quoteId: $quoteId"
    }
}
