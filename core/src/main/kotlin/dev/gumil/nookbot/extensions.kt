package dev.gumil.nookbot

import dev.gumil.nookbot.entities.MessageEntity
import dev.gumil.nookbot.exceptions.MessageEntityTypeNotSupported

/**
 * Only support bot commands
 */
internal const val BOT_COMMAND = "bot_command"

typealias CommandContent = Pair<String, String>

fun MessageEntity.extractCommand(text: String): CommandContent {
    if (type != BOT_COMMAND) throw MessageEntityTypeNotSupported(type)

    val startIndex = offset + 1
    val endIndex = if (text.contains('@')) {
        text.indexOf('@')
    } else {
        length
    }

    return text.substring(startIndex, endIndex).toLowerCase() to
            text.substring(length + 1).toLowerCase()
}
