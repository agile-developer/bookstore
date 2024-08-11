package co.bound.exercise.bookstore.statistics

import co.bound.exercise.bookstore.domain.event.BookSearched
import co.bound.exercise.bookstore.domain.event.OrderCreated
import co.bound.exercise.bookstore.statistics.service.StatisticsService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EventListeners(
    private val statisticsService: StatisticsService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onBookSearched(bookSearched: BookSearched) {
        logger.info("Book searched, ISBN: ${bookSearched.bookSummary.isbn}, title: ${bookSearched.bookSummary.title}")
        statisticsService.incrementBookSearchCount(bookSearched.bookSummary.isbn)
    }

    @EventListener
    fun onOrderCreated(orderCreated: OrderCreated) {
        logger.info("Book ordered, ISBN: ${orderCreated.order.bookQuote.booksSummary.isbn}," +
                " quoteId: ${orderCreated.order.bookQuote.id}")
        statisticsService.incrementBookOrderCount(orderCreated.order.bookQuote.booksSummary.isbn)
    }
}
