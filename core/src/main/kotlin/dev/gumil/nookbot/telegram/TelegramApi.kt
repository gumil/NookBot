package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.entities.telegram.Update

interface TelegramApi {
    suspend fun getUpdates(offset: Long, timeout: Int): List<Update>
}
