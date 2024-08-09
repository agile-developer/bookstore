package co.bound.exercise.bookstore.order.service

import co.bound.exercise.bookstore.domain.Order
import co.bound.exercise.bookstore.order.controller.CreateOrderRequest
import co.bound.exercise.bookstore.quote.service.QuoteService
import co.bound.exercise.thirdparties.valdivia.ValdiviaClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class OrderServiceImpl(
    private val quoteService: QuoteService,
    private val valdiviaClient: ValdiviaClient
) : OrderService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val createdOrders = ConcurrentHashMap<String, Order>()

    override fun createOrder(request: CreateOrderRequest): CreateOrderResult {
        if (createdOrders.containsKey(request.idempotencyId)) {
            logger.info("IdempotencyId: ${request.idempotencyId} already known, returning existing order")
            return CreateOrderResult.Success(createdOrders[request.idempotencyId]!!)
        }

        if (!quoteService.isValidQuote(request.quoteId)) {
            logger.error("QuoteId: ${request.quoteId} is not valid")
            return CreateOrderResult.Error(request.quoteId)
        }

        logger.info("Calling Valdivia to purchase quoteId: ${request.quoteId}")
        runCatching {
            valdiviaClient.purchaseBook(request.quoteId)
        }.getOrElse {
            logger.error("Error creating order for quoteId: ${request.quoteId}, message: ${it.message}")
            return CreateOrderResult.Error(request.quoteId)
        }

        val order = Order(
            idempotencyId = request.idempotencyId,
            quoteId = request.quoteId,
            status = Order.Status.CREATED,
            deliveryAddress = request.deliveryAddress
        )
        createdOrders[request.idempotencyId] = order
        quoteService.invalidateQuote(request.quoteId)
        return CreateOrderResult.Success(order)
    }
}
