package co.bound.exercise.bookstore.statistics.controller

import co.bound.exercise.bookstore.statistics.service.BookStatisticsResult
import co.bound.exercise.bookstore.statistics.service.StatisticsService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bookstore/statistics")
class StatisticsController(
    private val statisticsService: StatisticsService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getBookStatistics(
        @RequestParam isbn: String
    ): ResponseEntity<*> {
        if (isbn.isBlank()) {
            return ResponseEntity.badRequest().body("ISBN cannot be empty")
        }
        return when(val statisticsResult = statisticsService.getBookStatistics(isbn)) {
            is BookStatisticsResult.Found -> {
                val stats = statisticsResult.bookStatistics
                ResponseEntity.ok(BookStatisticsResponse(stats.isbn, stats.searchCount, stats.orderCount))
            }

            is BookStatisticsResult.NotFound -> {
                ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, statisticsResult.message))
                    .build<String>()
            }
        }
    }
}
