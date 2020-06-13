package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order

internal class InMemoryOrdersRepository : OrdersRepository {

    private val ordersMap = mutableMapOf<Long, MutableList<Order>>()

    override fun save(id: Long, order: Order) {
        ordersMap[id]?.add(order) ?: ordersMap.put(id, mutableListOf(order))
    }

    override fun getOrders(id: Long): List<Order>? {
        return ordersMap[id]
    }

    override fun getOrder(chatId: Long, orderId: Long): Order? {
        return getOrders(chatId)?.find { it.id == orderId }
    }

    override fun deleteOrder(id: Long, order: Order): Boolean {
        return ordersMap[id]?.remove(order) ?: false
    }
}
