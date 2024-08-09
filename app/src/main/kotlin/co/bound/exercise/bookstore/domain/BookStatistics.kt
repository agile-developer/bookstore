package co.bound.exercise.bookstore.domain

data class BookStatistics(
    val isbn: String,
    val searchCount: Int = 0,
    val orderCount: Int = 0) {

    fun incrementSearchCount(): BookStatistics {
        return BookStatistics(this.isbn, this.searchCount + 1, this.orderCount)
    }

    fun incrementOrderCount(): BookStatistics {
        return BookStatistics(this.isbn, this.searchCount, this.orderCount + 1)
    }
}
