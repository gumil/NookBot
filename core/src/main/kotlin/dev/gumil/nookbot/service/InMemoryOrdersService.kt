package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order

internal class InMemoryOrdersService : OrdersService {

    private val ordersMap = mutableMapOf<Int, MutableList<Order>>()

    override fun save(id: Int, order: Order) {
        ordersMap[id]?.add(order) ?: ordersMap.put(id, mutableListOf(order))
    }

    override fun getOrders(id: Int): List<Order>? {
        return ordersMap[id]
    }

    override fun sendOrder(id: Int, order: Order): Boolean {
        return ordersMap[id]?.remove(order) ?: false
    }
}
