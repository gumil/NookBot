package dev.gumil.nookbot.exceptions

internal class MessageEntityTypeNotSupported(type: String): Throwable("Entity type: $type not supported")
