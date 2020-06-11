package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order

interface OrdersService {

    fun save(id: Int, order: Order)

    fun getOrders(id: Int): List<Order>?

    /**
     * @return true when order was successfully sent
     */
    fun sendOrder(id: Int, order: Order): Boolean
}
