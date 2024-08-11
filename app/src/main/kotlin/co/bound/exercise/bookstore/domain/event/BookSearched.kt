package co.bound.exercise.bookstore.domain.event

import co.bound.exercise.bookstore.domain.BookSummary
import org.springframework.context.ApplicationEvent
import java.util.UUID

data class BookSearched(
    val id: String = UUID.randomUUID().toString(),
    val eventSource: Any,
    val bookSummary: BookSummary
) : ApplicationEvent(eventSource)
