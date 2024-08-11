package co.bound.exercise.bookstore.catalog.service

import co.bound.exercise.bookstore.domain.BookSummary
import co.bound.exercise.thirdparties.boogle.BoogleClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CatalogServiceImpl(
    private val boogleClient: BoogleClient,
) : CatalogService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun searchByAuthor(author: String): List<BookSummary> {
        logger.info("Calling Boogle to search for books by author: $author")
        val results = boogleClient.searchBooksByAuthor(author)
        return results.map { BookSummary(it.isbn, it.title, it.author) }
    }

    override fun findByIsbn(isbn: String): BookSummary? {
        logger.info("Calling Boogle to find single book by ISBN: $isbn")
        val result = boogleClient.findBookByIsbn(isbn).getOrNull()
        if (result == null) {
            logger.warn("No book found for ISBN: $isbn")
        }
        return result?.let { BookSummary(result.isbn, result.title, result.author) }
    }

}
