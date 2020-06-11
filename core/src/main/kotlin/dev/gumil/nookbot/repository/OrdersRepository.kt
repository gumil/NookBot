package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order

interface OrdersRepository {

    fun save(id: Int, order: Order)

    fun getOrders(id: Int): List<Order>?

    fun deleteOrder(id: Int, order: Order): Boolean
}
