package co.bound.exercise.bookstore.order.service

import co.bound.exercise.bookstore.domain.Order
import co.bound.exercise.bookstore.order.controller.CreateOrderRequest
import co.bound.exercise.bookstore.quote.service.QuoteService
import co.bound.exercise.thirdparties.valdivia.ValdiviaClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    private val quoteService: QuoteService,
    private val valdiviaClient: ValdiviaClient,
    private val orderRepository: OrderRepository
) : OrderService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun createOrder(request: CreateOrderRequest): CreateOrderResult {
        if (orderRepository.isCreated(request)) {
            logger.info("IdempotencyId: ${request.idempotencyId} already known, returning existing order")
            return CreateOrderResult.Success(orderRepository.findByIdempotencyId(request.idempotencyId)!!)
        }

        val bookQuote = quoteService.getQuote(request.quoteId)
        if (bookQuote == null) {
            logger.error("QuoteId: ${request.quoteId} is not valid")
            return CreateOrderResult.Error(request.quoteId)
        }

        // Multiple threads trying to create an order for a quote should synchronize on that quote instance
        synchronized(bookQuote) {
            logger.info("Calling Valdivia to purchase quoteId: ${request.quoteId}")
            runCatching {
                valdiviaClient.purchaseBook(request.quoteId)
            }.getOrElse {
                logger.error("Error creating order for quoteId: ${request.quoteId}, message: ${it.message}")
                return CreateOrderResult.Error(request.quoteId)
            }

            val order = Order(
                idempotencyId = request.idempotencyId,
                bookQuote = bookQuote,
                status = Order.Status.CREATED,
                deliveryAddress = request.deliveryAddress
            )
            orderRepository.save(request.idempotencyId, order)
            quoteService.invalidateQuote(request.quoteId)
            return CreateOrderResult.Success(order)
        }
    }
}
