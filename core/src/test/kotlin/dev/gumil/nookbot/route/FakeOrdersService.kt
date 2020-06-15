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
    private var sentOrder: Pair<Long, Resident>? = null

    override suspend fun saveOrder(chatId: Long, order: Order) {
        savedOrder = chatId to order
    }

    override suspend fun takeOrder(chatId: Long, messageId: Long, orderId: Long, seller: Resident) {
        takeOrder = OrderTaken(chatId, messageId, orderId, seller)
    }

    override suspend fun listOrder(chatId: Long) {
        listOrderId = chatId
    }

    override suspend fun markOrderSent(chatId: Long, seller: Resident) {
        sentOrder = chatId to seller
    }

    fun verifySavedOrder(chatId: Long, order: Order) {
        assertEquals(chatId, savedOrder?.first)
        assertEquals(order, savedOrder?.second)
    }

    fun verifyEmptyOrder() {
        assertNull(savedOrder)
    }

    fun verifyOrderTaken(chatId: Long, messageId: Long, orderId: Long, seller: Resident) {
        assertEquals(chatId, takeOrder?.id)
        assertEquals(messageId, takeOrder?.messageId)
        assertEquals(orderId, takeOrder?.orderId)
        assertEquals(seller, takeOrder?.seller)
    }

    fun verifyListOrder(chatId: Long) {
        assertEquals(chatId, listOrderId)
    }

    fun verifySentOrder(chatId: Long, seller: Resident) {
        assertEquals(chatId, sentOrder?.first)
        assertEquals(seller, sentOrder?.second)
    }

    fun tearDown() {
        takeOrder = null
        savedOrder = null
        sentOrder = null
    }

    data class OrderTaken(
        val id: Long,
        val messageId: Long,
        val orderId: Long,
        val seller: Resident
    )
}
