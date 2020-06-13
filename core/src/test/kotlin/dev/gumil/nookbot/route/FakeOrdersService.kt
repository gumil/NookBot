package dev.gumil.nookbot.route

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.service.OrdersService
import dev.gumil.nookbot.utils.TestInMemoryStore

internal class FakeOrdersService: OrdersService {
    private val store = TestInMemoryStore<Order>()

    override suspend fun saveOrder(id: Long, order: Order) {
        store.save(id, order)
    }

    override suspend fun takeOrder(id: Long, messageId: Long, orderId: Long, seller: Resident) {
        TODO("Not yet implemented")
    }

    fun verifySavedOrder(id: Long, order: Order) {
        store.verifySavedOrder(id, order)
    }

    fun verifyEmptyOrder() {
        store.verifyEmptyStore()
    }

    fun tearDown() {
        store.tearDown()
    }
}
