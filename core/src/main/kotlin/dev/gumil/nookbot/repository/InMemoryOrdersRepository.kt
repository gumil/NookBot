package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident

internal class InMemoryOrdersRepository : OrdersRepository {

    private val ordersMap = mutableMapOf<Long, MutableList<Order>>()

    override fun save(id: Long, order: Order) {
        val orders = ordersMap[id]

        if (orders == null) {
            ordersMap[id] = mutableListOf(order)
            return
        }

        val indexOrder = orders.indexOfFirst { it.id == order.id }

        if (indexOrder > -1) {
            orders[indexOrder] = order
            return
        }

        orders.add(order)
    }

    override fun getOrders(id: Long): List<Order>? {
        return ordersMap[id]
    }

    override fun getOrder(chatId: Long, orderId: OrderId): Order? {
        return getOrders(chatId)?.find { it.id == orderId.id }
    }

    override fun getOrder(chatId: Long, seller: SellerId): Order? {
        return getOrders(chatId)?.find { it.seller?.id == seller.id }
    }

    override fun hasPendingOrder(chatId: Long, seller: Resident): Boolean {
        return getOrders(chatId)?.any { order ->
            seller == order.seller
        } ?: false
    }

    override fun deleteOrder(id: Long, order: Order): Boolean {
        return ordersMap[id]?.remove(order) ?: false
    }
}
