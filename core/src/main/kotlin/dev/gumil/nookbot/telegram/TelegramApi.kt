package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.entities.telegram.Update

interface TelegramApi {
    suspend fun getUpdates(offset: Int, timeout: Int): List<Update>
}
