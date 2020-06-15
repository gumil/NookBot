package dev.gumil.nookbot.repository

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
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
    fun `getOrders should only get order list from id`() {
        val chatId = Random.nextLong()
        val otherChatId = Random.nextLong()
        val order = createOrder()
        val otherOrder = createOrder()

        ordersRepository.save(chatId, order)
        ordersRepository.save(otherChatId, otherOrder)
        val actual = ordersRepository.getOrders(chatId)

        assertEquals(listOf(order), actual)
    }

    @Test
    fun `get order returns specific order`() {
        val chatId = Random.nextLong()
        val orderId = Random.nextLong()
        val order = createOrder(orderId)

        ordersRepository.save(chatId, order)
        val actual = ordersRepository.getOrder(chatId, orderId)

        assertEquals(order, actual)
    }

    @Test
    fun `get order returns null`() {
        val chatId = Random.nextLong()
        val otherOrderId = Random.nextLong()
        val order = createOrder()

        ordersRepository.save(chatId, order)
        val actual = ordersRepository.getOrder(chatId, otherOrderId)

        assertNull(actual)
    }

    @Test
    fun `hasPendingOrders returns true when there is an order`() {
        val chatId = Random.nextLong()
        val seller = Resident(
            Random.nextLong(),
            Random.nextInt().toString()
        )
        val order = Order(
            Random.nextLong(),
            Random.nextInt().toString(),
            false,
            Resident(
                Random.nextLong(),
                Random.nextInt().toString()
            ),
            seller
        )

        ordersRepository.save(chatId, order)

        val actual = ordersRepository.hasPendingOrder(chatId, seller)

        assertTrue(actual)
    }

    @Test
    fun `hasPendingOrders returns false when order is empty`() {
        val chatId = Random.nextLong()
        val seller = Resident(
            Random.nextLong(),
            Random.nextInt().toString()
        )
        val actual = ordersRepository.hasPendingOrder(chatId, seller)

        assertFalse(actual)
    }

    @Test
    fun `hasPendingOrders returns false when order not found`() {
        val chatId = Random.nextLong()
        val seller = Resident(
            Random.nextLong(),
            Random.nextInt().toString()
        )
        val sellerWithOrder = Resident(
            Random.nextLong(),
            Random.nextInt().toString()
        )
        val order = Order(
            Random.nextLong(),
            Random.nextInt().toString(),
            false,
            Resident(
                Random.nextLong(),
                Random.nextInt().toString()
            ),
            sellerWithOrder
        )

        ordersRepository.save(chatId, order)

        val actual = ordersRepository.hasPendingOrder(chatId, seller)

        assertFalse(actual)
    }

    private fun createOrder(id: Long = Random.nextLong()): Order {
        val buyer = Resident(
            Random.nextLong(),
            "buyer"
        )
        val seller = Resident(
            Random.nextLong(),
            "seller"
        )
        return Order(
            id,
            Random.nextDouble().toString(),
            Random.nextBoolean(),
            buyer,
            seller
        )
    }
}
