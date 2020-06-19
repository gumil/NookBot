package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.telegram.entities.CallbackQuery
import dev.gumil.nookbot.telegram.entities.Message
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.exceptions.CommandParsingError

interface CommandRouter {
    suspend fun route(update: Update) {
        update.callbackQuery?.let {
            handleCallbackQuery(update, it)
        } ?: handleMessage(update, update.message)
    }

    suspend fun route(update: Update, command: String, content: String)

    fun onParsingError(error: CommandParsingError)

    private suspend fun handleMessage(update: Update, message: Message?) {
        val text = message?.text
        message?.entities?.forEach { messageEntity ->
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

    @Suppress("ReturnCount")
    private suspend fun handleCallbackQuery(update: Update, callbackQuery: CallbackQuery) {
        val text = callbackQuery.data

        if (text.isNullOrEmpty()) {
            onParsingError(CommandParsingError.NoContent())
            return
        }

        if (text.first() != '/') {
            onParsingError(CommandParsingError.UnsupportedContent(text))
            return
        }

        val commandContent = text.split(" ")

        if (commandContent.size != 2) {
            onParsingError(CommandParsingError.NoContent())
            return
        }

        val command = commandContent.first().substring(1)
        val content = commandContent.last()

        val newMessage = callbackQuery.message?.copy(
            from = callbackQuery.from,
            text = callbackQuery.data
        )

        val newUpdate = update.copy(message = newMessage, callbackQuery = null)
        route(newUpdate, command, content)
    }
}
