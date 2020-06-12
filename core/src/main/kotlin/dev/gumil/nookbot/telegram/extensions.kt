package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.telegram.entities.MessageEntity
import dev.gumil.nookbot.telegram.exceptions.CommandNotSupported
import dev.gumil.nookbot.telegram.exceptions.MessageEntityTypeNotSupported
import dev.gumil.nookbot.telegram.exceptions.NoContentException
import dev.gumil.nookbot.route.Command

/**
 * Only support bot commands
 */
internal const val BOT_COMMAND = "bot_command"

internal typealias CommandContent = Pair<Command, String>

internal fun MessageEntity.extractCommand(text: String): CommandContent {
    if (type != BOT_COMMAND) throw MessageEntityTypeNotSupported(type)
    if (length >= text.length) throw NoContentException()

    val startIndex = offset + 1
    val endIndex = if (text.contains('@')) {
        text.indexOf('@')
    } else {
        length
    }

    val commandString = text.substring(startIndex, endIndex)

    val command = try {
        Command.valueOf(commandString.toUpperCase())
    } catch (e: IllegalArgumentException) {
        throw CommandNotSupported(commandString)
    }

    return command to text.substring(length + 1).toLowerCase()
}
