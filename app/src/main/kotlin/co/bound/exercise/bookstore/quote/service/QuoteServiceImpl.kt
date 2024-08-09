package co.bound.exercise.bookstore.quote.service

import co.bound.exercise.bookstore.domain.BookQuote
import co.bound.exercise.thirdparties.valdivia.ValdiviaClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class QuoteServiceImpl(
    private val valdiviaClient: ValdiviaClient
) : QuoteService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val createdQuotes = ConcurrentHashMap<String, BookQuote>()

    override fun getQuote(isbn: String): BookQuoteResult {
        logger.info("Calling Valdivia to get quote for ISBN: $isbn")
        val quote = valdiviaClient.getBookQuote(isbn)
        val bookQuote = BookQuote(quote.id, isbn, quote.price)
        createdQuotes[quote.id] = bookQuote
        return BookQuoteResult.Success(bookQuote)
    }

    override fun isValidQuote(quoteId: String): Boolean {
        return createdQuotes.containsKey(quoteId)
    }

    override fun invalidateQuote(quoteId: String): Boolean {
        if (createdQuotes.containsKey(quoteId)) {
            createdQuotes.remove(quoteId)
            return true
        }
        return false
    }
}
