package co.bound.exercise.bookstore.quote.controller

import co.bound.exercise.bookstore.quote.service.BookQuoteResult
import co.bound.exercise.bookstore.quote.service.QuoteService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bookstore/quote")
class QuoteController(
    private val quoteService: QuoteService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping()
    fun getQuote(
        @RequestParam isbn: String
    ): ResponseEntity<*> {
        if (isbn.isBlank()) {
            return ResponseEntity.badRequest().body("ISBN cannot be empty")
        }
        return when (val bookQuoteResult = quoteService.getQuote(isbn)) {
            is BookQuoteResult.Success -> {
                val bookQuote = bookQuoteResult.bookQuote
                ResponseEntity.ok(QuoteResponse(bookQuote.id, bookQuote.isbn, bookQuote.price))
            }

            is BookQuoteResult.Error -> {
                ResponseEntity.badRequest().body(bookQuoteResult.message)
            }
        }
    }
}