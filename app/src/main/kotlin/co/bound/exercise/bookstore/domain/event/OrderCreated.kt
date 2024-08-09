package co.bound.exercise.bookstore.domain.event

import co.bound.exercise.bookstore.domain.Order
import org.springframework.context.ApplicationEvent

data class OrderCreated(val eventSource: Any, val order: Order) : ApplicationEvent(eventSource)
