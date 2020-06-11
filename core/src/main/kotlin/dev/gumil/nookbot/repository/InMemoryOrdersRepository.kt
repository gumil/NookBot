package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order

internal class InMemoryOrdersRepository : OrdersRepository {

    private val ordersMap = mutableMapOf<Int, MutableList<Order>>()

    override fun save(id: Int, order: Order) {
        ordersMap[id]?.add(order) ?: ordersMap.put(id, mutableListOf(order))
    }

    override fun getOrders(id: Int): List<Order>? {
        return ordersMap[id]
    }

    override fun deleteOrder(id: Int, order: Order): Boolean {
        return ordersMap[id]?.remove(order) ?: false
    }
}
