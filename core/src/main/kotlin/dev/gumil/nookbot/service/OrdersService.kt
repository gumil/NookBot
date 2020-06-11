package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order

interface OrdersService {

    suspend fun saveOrder(id: Long, order: Order)
}
