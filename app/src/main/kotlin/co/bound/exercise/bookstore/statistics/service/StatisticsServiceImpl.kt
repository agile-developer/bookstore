package co.bound.exercise.bookstore.statistics.service

import co.bound.exercise.bookstore.catalog.service.CatalogService
import co.bound.exercise.bookstore.domain.BookStatistics
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class StatisticsServiceImpl(
    val catalogServiceImpl: CatalogService
) : StatisticsService {

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

    override fun incrementBookSearchCount(isbn: String): Int {
        logger.info("Updating search count for ISBN: $isbn")
        var statistics = bookStatisticsRepo[isbn]
        if (statistics != null) {
            statistics = statistics.incrementSearchCount()
            bookStatisticsRepo.replace(isbn, statistics)
        } else {
            val bookSummary = catalogServiceImpl.findByIsbn(isbn)
            if (bookSummary == null) {
                logger.warn("No book found for ISBN: $isbn")
                return 0;
            }
            statistics = BookStatistics(bookSummary, 1, 0)
            bookStatisticsRepo[isbn] = statistics
        }
        return statistics.searchCount
    }

    override fun incrementBookOrderCount(isbn: String): Int {
        logger.info("Updating order count for ISBN: $isbn")
        var statistics = bookStatisticsRepo[isbn]
        if (statistics != null) {
            statistics = statistics.incrementOrderCount()
            bookStatisticsRepo.replace(isbn, statistics)
        } else {
            val bookSummary = catalogServiceImpl.findByIsbn(isbn)
            if (bookSummary == null) {
                logger.warn("No book found for ISBN: $isbn")
                return 0;
            }
            statistics = BookStatistics(bookSummary, 0, 1)
            bookStatisticsRepo[isbn] = statistics
        }
        return statistics.orderCount
    }
}
