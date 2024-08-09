package co.bound.exercise.bookstore.statistics.service

import co.bound.exercise.bookstore.domain.BookStatistics

interface StatisticsService {

    fun getBookStatistics(isbn: String): BookStatisticsResult

    fun updateBookSearchCount(isbn: String): Int

    fun updateBookOrderCount(isbn: String): Int
}

sealed interface BookStatisticsResult {
    data class Found(val bookStatistics: BookStatistics) : BookStatisticsResult
    data class NotFound(val isbn: String) : BookStatisticsResult {
        val message get() = "No statistics to report for ISBN: $isbn"
    }
}