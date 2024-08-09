package co.bound.exercise.bookstore.statistics.controller

data class BookStatisticsResponse(
    val isbn: String,
    val searchCount: Int,
    val orderCount: Int
)
