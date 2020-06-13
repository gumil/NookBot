package dev.gumil.nookbot.service

import dev.gumil.nookbot.telegram.entities.Message
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.TelegramApi
import dev.gumil.nookbot.telegram.request.EditMessageRequest
import dev.gumil.nookbot.telegram.request.SendMessageRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

internal class FakeTelegramApi : TelegramApi {

    private var updates: (() -> List<Update>)? = null

    private var sentMessage: (() -> Message)? = null

    private var editedMessage: (() -> Message)? = null

    private var actualMessageSent: Message? = null

    private var actualMessageEdited: Message? = null

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

    override suspend fun editMessageMarkUp(editMessageRequest: EditMessageRequest): Message {
        assertNotNull(editedMessage)
        return editedMessage!!.invoke().also {
            actualMessageEdited = it
        }
    }


    fun givenSentMessage(message: Message) {
        sentMessage = { message }
    }

    fun givenEditedMessage(message: Message) {
        editedMessage = { message }
    }

    fun verifyMessageSent(message: Message) {
        assertEquals(message, actualMessageSent)
    }

    fun verifyMessageEdited(message: Message) {
        assertEquals(message, actualMessageEdited)
    }

    fun tearDown() {
        updates = null
        sentMessage = null
        editedMessage = null
        actualMessageEdited = null
        actualMessageSent = null
    }
}
