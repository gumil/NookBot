package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.telegram.entities.MessageEntity
import dev.gumil.nookbot.telegram.exceptions.CommandParsingError

/**
 * Only support bot commands
 */
internal const val BOT_COMMAND = "bot_command"

internal typealias CommandContent = Pair<String, String>

internal fun MessageEntity.extractCommand(text: String): CommandContent {
    if (type != BOT_COMMAND) throw CommandParsingError.MessageEntityTypeNotSupported(type)
    if (length >= text.trim().length) throw CommandParsingError.NoContent()

    val startIndex = offset + 1
    val endIndex = if (text.contains('@')) {
        text.indexOf('@')
    } else {
        length
    }

    return text.substring(startIndex, endIndex).toLowerCase() to
            text.substring(length + 1).toLowerCase()
}
