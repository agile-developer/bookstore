package co.bound.exercise.bookstore.domain

import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val idempotencyId: String,
    val bookQuote: BookQuote,
    val status: Status,
    val deliveryAddress: Address,
) {
    enum class Status {
        CREATED,
        CANCELLED,
        DELIVERED
    }
}
