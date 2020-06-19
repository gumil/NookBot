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

    val startIndex = offset + 1
    val endIndex = if (text.contains('@')) {
        text.indexOf('@')
    } else {
        length
    }

    val content = if (text.length == length) {
        ""
    } else {
        text.substring(length).trim().toLowerCase()
    }

    return text.substring(startIndex, endIndex).toLowerCase() to content
}
