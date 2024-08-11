package co.bound.exercise.bookstore.statistics.service

import co.bound.exercise.bookstore.catalog.service.CatalogServiceImpl
import co.bound.exercise.thirdparties.boogle.BoogleClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StatisticsServiceTest {

    @Test
    fun `should return statistics 'NotFound' for ISBN for which counters have not been updated`() {
        // arrange
        val statisticsService = StatisticsServiceImpl(CatalogServiceImpl(BoogleClient()))
        val isbn = "978-0141184999"

        // act
        val result = statisticsService.getBookStatistics(isbn)

        //assert
        assertThat(result).isInstanceOf(BookStatisticsResult.NotFound::class.java)
    }

    @Test
    fun `should return 'Found' with the correct search count`() {
        // arrange
        val statisticsService = StatisticsServiceImpl(CatalogServiceImpl(BoogleClient()))
        val isbn = "978-0141184999"

        // act & assert
        var searchCount = statisticsService.incrementBookSearchCount(isbn)
        assertThat(searchCount).isEqualTo(1)
        statisticsService.incrementBookSearchCount(isbn)
        searchCount = statisticsService.incrementBookSearchCount(isbn)
        assertThat(searchCount).isEqualTo(3)

        val result = statisticsService.getBookStatistics(isbn)
        assertThat(result).isInstanceOf(BookStatisticsResult.Found::class.java)
        val stats = (result as BookStatisticsResult.Found).bookStatistics
        assertThat(stats.searchCount).isEqualTo(3)
        assertThat(stats.orderCount).isEqualTo(0)
    }

    @Test
    fun `should return 'Found' with the correct order count`() {
        // arrange
        val statisticsService = StatisticsServiceImpl(CatalogServiceImpl(BoogleClient()))
        val isbn = "978-0141184999"

        // act & assert
        var orderCount = statisticsService.incrementBookOrderCount(isbn)
        assertThat(orderCount).isEqualTo(1)
        statisticsService.incrementBookOrderCount(isbn)
        orderCount = statisticsService.incrementBookOrderCount(isbn)
        assertThat(orderCount).isEqualTo(3)

        val result = statisticsService.getBookStatistics(isbn)
        assertThat(result).isInstanceOf(BookStatisticsResult.Found::class.java)
        val stats = (result as BookStatisticsResult.Found).bookStatistics
        assertThat(stats.orderCount).isEqualTo(3)
        assertThat(stats.searchCount).isEqualTo(0)
    }
}
