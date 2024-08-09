package co.bound.exercise.bookstore.quote.service

import co.bound.exercise.bookstore.domain.BookQuote

interface QuoteService {

    fun getQuote(isbn: String): BookQuoteResult

    fun isValidQuote(quoteId: String): Boolean

    fun invalidateQuote(quoteId: String): Boolean
}

sealed interface BookQuoteResult {
    data class Success(val bookQuote: BookQuote) : BookQuoteResult
    data class Error(val isbn: String) : BookQuoteResult {
        val message get() = "Error creating quote for ISBN: $isbn"
    }
}
