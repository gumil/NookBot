package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.telegram.Message
import dev.gumil.nookbot.entities.telegram.Update
import dev.gumil.nookbot.telegram.TelegramApi
import dev.gumil.nookbot.telegram.request.SendMessageRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

internal class FakeTelegramApi : TelegramApi {

    private var updates: (() -> List<Update>)? = null

    private var sentMessage: (() -> Message)? = null

    private var actualMessageSent: Message? = null

    override suspend fun getUpdates(offset: Long, timeout: Int): List<Update> {
        assertNotNull(updates)
        return updates!!.invoke()
    }

    override suspend fun sendMessage(sendMessageRequest: SendMessageRequest): Message {
        assertNotNull(sentMessage)
        return sentMessage!!.invoke().also {
            actualMessageSent = it
        }
    }


    fun givenSentMessage(message: Message) {
        sentMessage = { message }
    }

    fun verifyMessageSent(message: Message) {
        assertEquals(message, actualMessageSent)
    }

    fun tearDown() {
        updates = null
        sentMessage = null
    }
}
