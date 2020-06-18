package dev.gumil.nookbot.repository

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.utils.LocalDynamoDBCreationExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.random.Random

internal class DynamoOrdersRepositoryTest {

    private lateinit var ordersRepository: DynamoOrdersRepository

    @BeforeEach
    fun setUp() {
        ordersRepository = DynamoOrdersRepository(server.amazonDynamoDB)
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
    fun `save with same order should overwrite order`() {
        val chatId = Random.nextLong()
        val order = createOrder()
        val newOrder = order.copy(seller = Resident(Random.nextLong(), Random.nextInt().toString()))

        ordersRepository.save(chatId, order)
        ordersRepository.save(chatId, newOrder)
        val actual = ordersRepository.getOrders(chatId)

        assertEquals(listOf(newOrder), actual)
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
        val actual = ordersRepository.getOrder(chatId, OrderId(orderId))

        assertEquals(order, actual)
    }

    @Test
    fun `get order returns null`() {
        val chatId = Random.nextLong()
        val otherOrderId = Random.nextLong()
        val order = createOrder()

        ordersRepository.save(chatId, order)
        val actual = ordersRepository.getOrder(chatId, OrderId(otherOrderId))

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

    @Test
    fun `get order with seller id returns specific order`() {
        val chatId = Random.nextLong()
        val orderId = Random.nextLong()
        val sellerId = Random.nextLong()
        val order = createOrder(orderId, sellerId)

        ordersRepository.save(chatId, order)
        val actual = ordersRepository.getOrder(chatId, SellerId(sellerId))

        assertEquals(order, actual)
    }

    @Test
    fun `get order with seller id when order not found returns null`() {
        val chatId = Random.nextLong()
        val orderId = Random.nextLong()
        val sellerId = Random.nextLong()
        val otherSellerId = Random.nextLong()
        val order = createOrder(orderId, sellerId)

        ordersRepository.save(chatId, order)
        val actual = ordersRepository.getOrder(chatId, SellerId(otherSellerId))

        assertNull(actual)
    }

    private fun createOrder(
        orderId: Long = Random.nextLong(),
        sellerId: Long = Random.nextLong()
    ): Order {
        val buyer = Resident(
            Random.nextLong(),
            "buyer"
        )
        val seller = Resident(
            sellerId,
            "seller"
        )
        return Order(
            orderId,
            Random.nextDouble().toString(),
            buyer,
            seller
        )
    }

    companion object {
        private const val PRIMARY_KEY = "id"

        @JvmField
        @RegisterExtension
        val server = LocalDynamoDBCreationExtension(TABLE_NAME, PRIMARY_KEY)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            server.amazonDynamoDB.createTable(
                CreateTableRequest()
                    .withTableName(TABLE_NAME)
                    .withAttributeDefinitions(
                        AttributeDefinition()
                            .withAttributeName(PRIMARY_KEY)
                            .withAttributeType("N")
                    )
                    .withKeySchema(
                        KeySchemaElement()
                            .withAttributeName(PRIMARY_KEY)
                            .withKeyType("HASH")
                    )
                    .withProvisionedThroughput(
                        ProvisionedThroughput()
                            .withReadCapacityUnits(10)
                            .withWriteCapacityUnits(5)
                    )
            )
        }
    }
}
