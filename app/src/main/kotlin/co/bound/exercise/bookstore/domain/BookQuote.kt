package co.bound.exercise.bookstore.domain

data class BookQuote(
    val id: String,
    val price: Double,
    val booksSummary: BookSummary
)
