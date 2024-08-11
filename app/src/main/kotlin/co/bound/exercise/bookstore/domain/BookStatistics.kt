package co.bound.exercise.bookstore.domain

data class BookStatistics(
    val bookSummary: BookSummary,
    val searchCount: Int = 0,
    val orderCount: Int = 0) {

    fun incrementSearchCount(): BookStatistics {
        return BookStatistics(this.bookSummary, this.searchCount + 1, this.orderCount)
    }

    fun incrementOrderCount(): BookStatistics {
        return BookStatistics(this.bookSummary, this.searchCount, this.orderCount + 1)
    }
}
