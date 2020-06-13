package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident

interface OrdersService {

    suspend fun saveOrder(id: Long, order: Order)

    suspend fun takeOrder(id: Long, messageId: Long, orderId: Long, seller: Resident)
}
