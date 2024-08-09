package co.bound.exercise.bookstore.order.controller

import co.bound.exercise.bookstore.domain.Order

data class CreateOrderResponse(
    val id: String,
    val status: Order.Status,
    val quoteId: String
)
