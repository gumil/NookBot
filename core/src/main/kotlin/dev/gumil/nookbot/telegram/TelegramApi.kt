package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.entities.Update

interface TelegramApi {
    suspend fun getUpdates(limit: Int, offset: Int): List<Update>
}
