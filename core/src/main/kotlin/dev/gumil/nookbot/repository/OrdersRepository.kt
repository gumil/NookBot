package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident

interface OrdersRepository {

    fun save(id: Long, order: Order)

    fun getOrders(id: Long): List<Order>?

    fun getOrder(chatId: Long, orderId: OrderId): Order?

    fun getOrder(chatId: Long, seller: SellerId): Order?

    fun hasPendingOrder(chatId: Long, seller: Resident): Boolean

    fun deleteOrder(id: Long, order: Order): Boolean
}

inline class OrderId(val id: Long)
inline class SellerId(val id: Long)
