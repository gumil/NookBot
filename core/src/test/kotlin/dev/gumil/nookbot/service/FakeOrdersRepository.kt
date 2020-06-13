package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.repository.OrdersRepository
import dev.gumil.nookbot.utils.TestInMemoryStore

internal class FakeOrdersRepository: OrdersRepository {
    private val store = TestInMemoryStore<Order>()

    override fun save(id: Long, order: Order) {
        store.save(id, order)
    }

    override fun getOrders(id: Long): List<Order>? {
        return store.getItems(id)
    }

    override fun getOrder(chatId: Long, orderId: Long): Order? {
        return getOrders(chatId)?.find { it.id == orderId }
    }

    override fun deleteOrder(id: Long, order: Order): Boolean {
        return store.deleteItem(id, order)
    }

    fun verifySavedOrder(id: Long, order: Order) {
        store.verifySavedOrder(id, order)
    }

    fun verifyEmptyOrders() {
        store.verifyEmptyStore()
    }

    fun tearDown() {
        store.tearDown()
    }
}
