package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.repository.OrdersRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

internal class FakeOrdersRepository: OrdersRepository {
    private var ordersList: List<Order>? = null
    private var savedOrder: Pair<Long, Order>? = null
    private var givenOrder: Order? = null

    override fun save(id: Long, order: Order) {
        savedOrder = id to order
    }

    override fun getOrders(id: Long): List<Order>? {
        return ordersList
    }

    override fun getOrder(chatId: Long, orderId: Long): Order? {
        return givenOrder
    }

    override fun deleteOrder(id: Long, order: Order): Boolean {
        TODO()
    }

    fun givenOrder(order: Order) {
        givenOrder = order
    }

    fun givenListOfOrders(orders: List<Order>) {
        ordersList = orders
    }

    fun verifySavedOrder(id: Long, order: Order) {
        assertEquals(id, savedOrder?.first)
        assertEquals(order, savedOrder?.second)
    }

    fun verifyEmptyOrders() {
        assertNull(savedOrder)
    }

    fun verifyListOfOrders(orders: List<Order>) {
        assertEquals(orders, ordersList)
    }

    fun tearDown() {
        savedOrder = null
        ordersList = null
        givenOrder = null
    }
}
