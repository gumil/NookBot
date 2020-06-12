package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.telegram.entities.Message
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.request.SendMessageRequest

interface TelegramApi {
    suspend fun getUpdates(offset: Long, timeout: Int): List<Update>
    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): Message
}
