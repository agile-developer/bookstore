package co.bound.exercise.bookstore.domain.event

import co.bound.exercise.bookstore.domain.BookSummary
import org.springframework.context.ApplicationEvent

data class BookSearched(val eventSource: Any, val bookSummary: BookSummary) : ApplicationEvent(eventSource)
