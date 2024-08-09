package co.bound.exercise.bookstore.catalog.controller

import co.bound.exercise.bookstore.catalog.controller.SearchField.AUTHOR
import co.bound.exercise.bookstore.catalog.service.CatalogService
import co.bound.exercise.bookstore.domain.BookSummary
import co.bound.exercise.bookstore.domain.event.BookSearched
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bookstore/catalog")
class CatalogController(
    private val catalogService: CatalogService,
    private val publisher: ApplicationEventPublisher
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun searchCatalog(
        @RequestParam value: String,
        @RequestParam field: String
    ): ResponseEntity<*> {
        if (value.isBlank() || field.isBlank()) {
            return ResponseEntity.badRequest().body("Search value and field cannot be empty")
        }
        val searchField = runCatching {
            SearchField.valueOf(field.uppercase())
        }.getOrElse {
            return ResponseEntity.badRequest().body("Search field must be one of ${SearchField.entries.toTypedArray()}")
        }
        val searchResults = when {
            searchField == AUTHOR -> catalogService.searchByAuthor(value)
            else -> {
                return ResponseEntity.badRequest().body("Search by $searchField currently not supported")
            }
        }
        if (searchResults.isNotEmpty()) {
            logger.info("Found ${searchResults.size} books for $searchField: $value")
        } else {
            logger.info("No books found for $searchField: $value")
        }
        val bookSummaries = searchResults.map { BookSummary(it.isbn, it.title, it.author) }
        bookSummaries.forEach {
            publisher.publishEvent(BookSearched(this, it))
        }
        return ResponseEntity.ok(SearchResponse(bookSummaries))
    }
}
