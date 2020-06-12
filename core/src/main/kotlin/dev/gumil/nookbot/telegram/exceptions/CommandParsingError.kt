package dev.gumil.nookbot.telegram.exceptions

sealed class CommandParsingError(message: String): Throwable(message) {
    class MessageEntityTypeNotSupported(type: String): CommandParsingError("Entity type: $type not supported")
    class NoContent: CommandParsingError("No content found for command")
}
