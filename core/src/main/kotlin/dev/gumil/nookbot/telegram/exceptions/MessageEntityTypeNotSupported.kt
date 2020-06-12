package dev.gumil.nookbot.telegram.exceptions

internal class MessageEntityTypeNotSupported(type: String): Throwable("Entity type: $type not supported")
