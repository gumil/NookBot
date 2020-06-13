package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.telegram.entities.Chat
import dev.gumil.nookbot.telegram.entities.Message
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TelegramOrdersServiceTest {
    private val telegramApi = FakeTelegramApi()
    private val repository = FakeOrdersRepository()

    private val telegramOrdersService = TelegramOrdersService(repository, telegramApi)

    @Test
    fun `saveOrder saves order and sends message`() = runBlocking {
        val id = Random.nextLong()
        val order = Order(
            Random.nextLong(),
            Random.nextDouble().toString(),
            buyer = Resident(
                Random.nextLong(),
                Random.nextDouble().toString()
            )
        )

        val message = Message(
            Random.nextLong(),
            date = Random.nextLong(),
            chat = Chat(
                Random.nextLong(),
                Chat.Type.GROUP
            )
        )

        telegramApi.givenSentMessage(message)

        telegramOrdersService.saveOrder(id, order)

        repository.verifySavedOrder(id, order)
        telegramApi.verifyMessageSent(message)
    }

    @Test
    fun `takeOrder when order not found ignores operation`() = runBlocking {
        val id = Random.nextLong()
        val messageId = Random.nextLong()
        val orderId = Random.nextLong()

        val resident = Resident(
            Random.nextLong(),
            Random.nextDouble().toString()
        )

        telegramOrdersService.takeOrder(id, messageId, orderId, resident)

        repository.verifyEmptyOrders()
        // no further verification
    }

    @Test
    fun `takeOrder saves new order and edit message`() = runBlocking {
        val id = Random.nextLong()
        val messageId = Random.nextLong()
        val orderId = Random.nextLong()

        val order = Order(
            orderId,
            Random.nextDouble().toString(),
            buyer = Resident(
                Random.nextLong(),
                Random.nextDouble().toString()
            )
        )

        val messageEdited = Message(
            Random.nextLong(),
            date = Random.nextLong(),
            chat = Chat(
                Random.nextLong(),
                Chat.Type.GROUP
            )
        )

        val messageSent = Message(
            Random.nextLong(),
            date = Random.nextLong(),
            chat = Chat(
                Random.nextLong(),
                Chat.Type.GROUP
            )
        )

        val seller = Resident(
            Random.nextLong(),
            Random.nextDouble().toString()
        )

        val orderWithSeller = order.copy(seller = seller)

        repository.givenOrder(order)
        telegramApi.givenEditedMessage(messageEdited)
        telegramApi.givenSentMessage(messageSent)

        telegramOrdersService.takeOrder(id, messageId, orderId, seller)

        repository.verifySavedOrder(id, orderWithSeller)
        telegramApi.verifyMessageEdited(messageEdited)
        telegramApi.verifyMessageSent(messageSent)
    }

    @Test
    fun `listOrder getsOrder and sends message`() = runBlocking {
        val id = Random.nextLong()
        val order = Order(
            Random.nextLong(),
            Random.nextDouble().toString(),
            buyer = Resident(
                Random.nextLong(),
                Random.nextDouble().toString()
            )
        )

        val message = Message(
            Random.nextLong(),
            date = Random.nextLong(),
            chat = Chat(
                Random.nextLong(),
                Chat.Type.GROUP
            )
        )

        val orderList = listOf(order)
        repository.givenListOfOrders(orderList)
        telegramApi.givenSentMessage(message)

        telegramOrdersService.listOrder(id)

        repository.verifyListOfOrders(orderList)
        telegramApi.verifyMessageSent(message)
    }

    @Test
    fun `listOrder getsOrder when there are no orders still sends message`() = runBlocking {
        val id = Random.nextLong()

        val message = Message(
            Random.nextLong(),
            date = Random.nextLong(),
            chat = Chat(
                Random.nextLong(),
                Chat.Type.GROUP
            )
        )

        val orderList = emptyList<Order>()
        repository.givenListOfOrders(orderList)
        telegramApi.givenSentMessage(message)

        telegramOrdersService.listOrder(id)

        repository.verifyListOfOrders(orderList)
        telegramApi.verifyMessageSent(message)
    }

    @AfterEach
    fun tearDown() {
        telegramApi.tearDown()
        repository.tearDown()
    }
}
