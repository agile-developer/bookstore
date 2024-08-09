package co.bound.exercise.bookstore.statistics.service

import co.bound.exercise.bookstore.domain.BookStatistics
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class StatisticsServiceImpl : StatisticsService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val bookStatisticsRepo = ConcurrentHashMap<String, BookStatistics>()

    override fun getBookStatistics(isbn: String): BookStatisticsResult {
        logger.info("Fetching statistics for ISBN: $isbn")
        if (!bookStatisticsRepo.containsKey(isbn)) {
            logger.warn("No statistics to report for ISBN: $isbn")
            return BookStatisticsResult.NotFound(isbn)
        }
        return BookStatisticsResult.Found(bookStatisticsRepo[isbn]!!)
    }

    override fun updateBookSearchCount(isbn: String): Int {
        logger.info("Updating search count for ISBN: $isbn")
        var statistics = bookStatisticsRepo[isbn]
        if (statistics != null) {
            bookStatisticsRepo.replace(isbn, statistics.incrementSearchCount())
        } else {
            statistics = BookStatistics(isbn, 1, 0)
            bookStatisticsRepo[isbn] = statistics
        }
        return statistics.searchCount
    }

    override fun updateBookOrderCount(isbn: String): Int {
        logger.info("Updating order count for ISBN: $isbn")
        var statistics = bookStatisticsRepo[isbn]
        if (statistics != null) {
            bookStatisticsRepo.replace(isbn, statistics.incrementOrderCount())
        } else {
            statistics = BookStatistics(isbn, 0, 1)
            bookStatisticsRepo[isbn] = statistics
        }
        return statistics.orderCount
    }
}
