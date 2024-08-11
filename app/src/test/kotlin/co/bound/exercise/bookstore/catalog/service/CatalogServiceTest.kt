package co.bound.exercise.bookstore.catalog.service

import co.bound.exercise.thirdparties.boogle.BoogleClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CatalogServiceTest {

    private val boogleClient = BoogleClient()
    private val catalogService = CatalogServiceImpl(boogleClient)

    @Test
    fun `searching for a known author returns results`() {
        // arrange
        val author = "Gabriel Garcia Marquez"

        // act
        val result = catalogService.searchByAuthor(author)

        // assert
        assertThat(result).isNotEmpty
    }

    @Test
    fun `searching for an unknown author returns no results`() {
        // arrange
        val author = "Unknown"

        // act
        val result = catalogService.searchByAuthor(author)

        // assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `searching for a known ISBN returns results`() {
        // arrange
        val isbn = "978-0141989587"

        // act
        val result = catalogService.findByIsbn(isbn)

        // assert
        assertThat(result).isNotNull
    }

    @Test
    fun `searching for an unknown ISBN returns no results`() {
        // arrange
        val isbn = "Unknown"

        // act
        val result = catalogService.findByIsbn(isbn)

        // assert
        assertThat(result).isNull()
    }

}
