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

    @AfterEach
    fun tearDown() {
        telegramApi.tearDown()
        repository.tearDown()
    }
}
