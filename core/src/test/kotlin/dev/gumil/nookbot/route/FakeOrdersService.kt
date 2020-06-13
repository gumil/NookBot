package dev.gumil.nookbot.route

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.service.OrdersService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

internal class FakeOrdersService: OrdersService {
    private var savedOrder: Pair<Long, Order>? = null
    private var takeOrder: OrderTaken? = null
    private var listOrderId: Long? = null

    override suspend fun saveOrder(id: Long, order: Order) {
        savedOrder = id to order
    }

    override suspend fun takeOrder(id: Long, messageId: Long, orderId: Long, seller: Resident) {
        takeOrder = OrderTaken(id, messageId, orderId, seller)
    }

    override suspend fun listOrder(id: Long) {
        listOrderId = id
    }

    fun verifySavedOrder(id: Long, order: Order) {
        assertEquals(id, savedOrder?.first)
        assertEquals(order, savedOrder?.second)
    }

    fun verifyEmptyOrder() {
        assertNull(savedOrder)
    }

    fun verifyOrderTaken(id: Long, messageId: Long, orderId: Long, seller: Resident) {
        assertEquals(id, takeOrder?.id)
        assertEquals(messageId, takeOrder?.messageId)
        assertEquals(orderId, takeOrder?.orderId)
        assertEquals(seller, takeOrder?.seller)
    }

    fun verifyListOrder(id: Long) {
        assertEquals(id, listOrderId)
    }

    fun tearDown() {
        takeOrder = null
        savedOrder = null
    }

    data class OrderTaken(
        val id: Long,
        val messageId: Long,
        val orderId: Long,
        val seller: Resident
    )
}
