package co.bound.exercise.bookstore.domain.event

import co.bound.exercise.bookstore.domain.Order
import org.springframework.context.ApplicationEvent
import java.util.UUID

data class OrderCreated(
    val id: String = UUID.randomUUID().toString(),
    val eventSource: Any,
    val order: Order
) : ApplicationEvent(eventSource)
