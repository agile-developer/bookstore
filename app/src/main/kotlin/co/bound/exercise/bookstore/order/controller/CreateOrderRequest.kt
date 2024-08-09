package co.bound.exercise.bookstore.order.controller

import co.bound.exercise.bookstore.domain.Address

data class CreateOrderRequest(
    val idempotencyId: String,
    val quoteId: String,
    val deliveryAddress: Address
)
