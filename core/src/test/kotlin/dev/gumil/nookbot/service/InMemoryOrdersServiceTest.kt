package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class InMemoryOrdersServiceTest {

    private lateinit var ordersService: InMemoryOrdersService

    @BeforeEach
    fun setUp() {
        ordersService = InMemoryOrdersService()
    }

    @Test
    fun `save adds order to list of orders`() {
        val roomId = Random.nextInt()
        val order = createOrder()

        ordersService.save(roomId, order)
        val actual = ordersService.getOrders(roomId)

        assertEquals(listOf(order), actual)
    }

    @Test
    fun `send order removes order from list`() {
        val roomId = Random.nextInt()
        val order = createOrder()

        ordersService.save(roomId, order)
        val orderSent = ordersService.sendOrder(roomId, order)
        val orderList = ordersService.getOrders(roomId)

        assertTrue(orderSent)
        assertTrue(orderList?.isEmpty() == true)
    }

    @Test
    fun `send order returns false when order is not saved`() {
        val roomId = Random.nextInt()
        val order = createOrder()
        val orderNotSaved = createOrder()

        ordersService.save(roomId, order)
        val orderSent = ordersService.sendOrder(roomId, orderNotSaved)
        val orderList = ordersService.getOrders(roomId)

        assertFalse(orderSent)
        assertEquals(listOf(order), orderList)
    }

    @Test
    fun `get order should only get order list from id`() {
        val roomId = Random.nextInt()
        val otherRoomId = Random.nextInt()
        val order = createOrder()
        val otherOrder = createOrder()

        ordersService.save(roomId, order)
        ordersService.save(otherRoomId, otherOrder)
        val actual = ordersService.getOrders(roomId)

        assertEquals(listOf(order), actual)
    }

    private fun createOrder(): Order {
        val buyer = Resident(
            Random.nextInt(),
            "buyer"
        )
        val seller = Resident(
            Random.nextInt(),
            "seller"
        )
        return Order(
            Random.nextInt(),
            Random.nextDouble().toString(),
            Random.nextBoolean(),
            buyer,
            seller
        )
    }
}
