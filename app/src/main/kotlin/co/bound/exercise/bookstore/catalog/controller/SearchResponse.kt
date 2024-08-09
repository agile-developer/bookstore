package co.bound.exercise.bookstore.catalog.controller

import co.bound.exercise.bookstore.domain.BookSummary

data class SearchResponse(
    val books: List<BookSummary>
)
