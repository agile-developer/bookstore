package co.bound.exercise.bookstore.catalog.service

import co.bound.exercise.bookstore.domain.BookSummary

interface CatalogService {

    fun searchByAuthor(author: String): List<BookSummary>

    fun searchByIsbn(isbn: String): BookSummary?
}
