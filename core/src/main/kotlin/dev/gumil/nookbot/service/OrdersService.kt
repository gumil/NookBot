package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident

interface OrdersService {

    suspend fun saveOrder(chatId: Long, order: Order)

    suspend fun takeOrder(chatId: Long, messageId: Long, orderId: Long, seller: Resident)

    suspend fun listOrder(chatId: Long)

    suspend fun markOrderSent(chatId: Long, seller: Resident)
}
