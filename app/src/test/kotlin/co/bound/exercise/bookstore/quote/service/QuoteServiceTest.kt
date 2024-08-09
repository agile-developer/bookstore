package co.bound.exercise.bookstore.quote.service

import co.bound.exercise.thirdparties.valdivia.ValdiviaClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class QuoteServiceTest {

    private val valdiviaClient = ValdiviaClient()
    private val quoteService = QuoteServiceImpl(valdiviaClient)

    @Test
    fun `returns quote for ISBN`() {
        // arrange
        val isbn = "978-0141184999"

        // act
        val bookQuote = quoteService.getQuote(isbn)

        // assert
        assertThat(bookQuote).isInstanceOf(BookQuoteResult.Success::class.java)
        assertThat((bookQuote as BookQuoteResult.Success).bookQuote.id).isNotBlank()
    }

    @Test
    fun `returns true for valid quote`() {
        // arrange
        val isbn = "978-0141184999"
        val bookQuote = quoteService.getQuote(isbn) as BookQuoteResult.Success

        // act
        val result = quoteService.isValidQuote(bookQuote.bookQuote.id)

        // assert
        assertThat(result).isTrue()
    }

    @Test
    fun `returns false for invalid quote`() {
        // arrange
        val unknownQuote = UUID.randomUUID().toString()

        // act
        val result = quoteService.isValidQuote(unknownQuote)

        // assert
        assertThat(result).isFalse()

    }

    @Test
    fun `returns false after a quote has been invalidated`() {
        // arrange
        val isbn = "978-0141184999"
        val bookQuote = quoteService.getQuote(isbn) as BookQuoteResult.Success

        // act & assert
        val isValid = quoteService.isValidQuote(bookQuote.bookQuote.id)
        assertThat(isValid).isTrue()
        val invalidated = quoteService.invalidateQuote(bookQuote.bookQuote.id)
        assertThat(invalidated).isTrue()
        val isValidAfterInvalidation = quoteService.isValidQuote(bookQuote.bookQuote.id)
        assertThat(isValidAfterInvalidation).isFalse()
    }
}
