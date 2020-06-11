package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order

interface OrdersRepository {

    fun save(id: Long, order: Order)

    fun getOrders(id: Long): List<Order>?

    fun deleteOrder(id: Long, order: Order): Boolean
}
