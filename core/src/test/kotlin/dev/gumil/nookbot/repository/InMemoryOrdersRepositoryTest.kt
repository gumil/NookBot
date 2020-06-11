package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class InMemoryOrdersRepositoryTest {

    private lateinit var ordersRepository: InMemoryOrdersRepository

    @BeforeEach
    fun setUp() {
        ordersRepository = InMemoryOrdersRepository()
    }

    @Test
    fun `save adds order to list of orders`() {
        val chatId = Random.nextLong()
        val order = createOrder()

        ordersRepository.save(chatId, order)
        val actual = ordersRepository.getOrders(chatId)

        assertEquals(listOf(order), actual)
    }

    @Test
    fun `delete order removes order from list`() {
        val chatId = Random.nextLong()
        val order = createOrder()

        ordersRepository.save(chatId, order)
        val orderSent = ordersRepository.deleteOrder(chatId, order)
        val orderList = ordersRepository.getOrders(chatId)

        assertTrue(orderSent)
        assertTrue(orderList?.isEmpty() == true)
    }

    @Test
    fun `delete order returns false when order is not saved`() {
        val chatId = Random.nextLong()
        val order = createOrder()
        val orderNotSaved = createOrder()

        ordersRepository.save(chatId, order)
        val orderSent = ordersRepository.deleteOrder(chatId, orderNotSaved)
        val orderList = ordersRepository.getOrders(chatId)

        assertFalse(orderSent)
        assertEquals(listOf(order), orderList)
    }

    @Test
    fun `get order should only get order list from id`() {
        val chatId = Random.nextLong()
        val otherChatId = Random.nextLong()
        val order = createOrder()
        val otherOrder = createOrder()

        ordersRepository.save(chatId, order)
        ordersRepository.save(otherChatId, otherOrder)
        val actual = ordersRepository.getOrders(chatId)

        assertEquals(listOf(order), actual)
    }

    private fun createOrder(): Order {
        val buyer = Resident(
            Random.nextLong(),
            "buyer"
        )
        val seller = Resident(
            Random.nextLong(),
            "seller"
        )
        return Order(
            Random.nextLong(),
            Random.nextDouble().toString(),
            Random.nextBoolean(),
            buyer,
            seller
        )
    }
}
