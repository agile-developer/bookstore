package co.bound.exercise.bookstore.order.service

import co.bound.exercise.bookstore.catalog.service.CatalogServiceImpl
import co.bound.exercise.bookstore.domain.Address
import co.bound.exercise.bookstore.order.controller.CreateOrderRequest
import co.bound.exercise.bookstore.quote.service.BookQuoteResult
import co.bound.exercise.bookstore.quote.service.QuoteServiceImpl
import co.bound.exercise.thirdparties.boogle.BoogleClient
import co.bound.exercise.thirdparties.valdivia.ValdiviaClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class OrderServiceTest {

    private val valdiviaClient = ValdiviaClient()
    private val catalogService = CatalogServiceImpl(BoogleClient())
    private val quoteService = QuoteServiceImpl(valdiviaClient, catalogService)
    private val orderService = OrderServiceImpl(quoteService, valdiviaClient, OrderRepository())

    @Test
    fun `should create an order when provided a valid request`() {
        // arrange
        val isbn = "978-0141184999"
        val bookQuoteResult = quoteService.createQuote(isbn) as BookQuoteResult.Success
        val orderRequest = CreateOrderRequest(UUID.randomUUID().toString(),
            bookQuoteResult.bookQuote.id, Address(
                "Wendover Court",
                "Finchley Road",
                "NW2 2PD",
                "London",
                "United Kingdom")
        )

        // act
        val result = orderService.createOrder(orderRequest)

        // assert
        assertThat(result).isInstanceOf(CreateOrderResult.Success::class.java)
        val orderResult = result as CreateOrderResult.Success
        assertThat(orderResult.order.bookQuote.id).isEqualTo(bookQuoteResult.bookQuote.id)
    }

    @Test
    fun `returns Error when quoteId is not valid`() {
        // arrange
        val orderRequest = CreateOrderRequest(UUID.randomUUID().toString(),
            UUID.randomUUID().toString(), Address(
                "Wendover Court",
                "Finchley Road",
                "NW2 2PD",
                "London",
                "United Kingdom")
        )

        // act
        val result = orderService.createOrder(orderRequest)

        // assert
        assertThat(result).isInstanceOf(CreateOrderResult.Error::class.java)
        val orderResult = result as CreateOrderResult.Error
        assertThat(orderResult.message).isEqualTo("Error creating order for quoteId: ${orderRequest.quoteId}")
    }
}
