package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.exceptions.CommandParsingError

interface CommandRouter {
    suspend fun route(update: Update) {
        val text = update.message?.text
        update.message?.entities?.forEach { messageEntity ->
            if (text != null) {
                try {
                    val (command, content) = messageEntity.extractCommand(text)
                    route(update, command, content)
                } catch (e: CommandParsingError) {
                    onParsingError(e)
                }
            }
        }
    }

    suspend fun route(update: Update, command: String, content: String)

    fun onParsingError(error: CommandParsingError)
}
