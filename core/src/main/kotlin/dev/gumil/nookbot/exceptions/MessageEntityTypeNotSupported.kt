package dev.gumil.nookbot.exceptions

class MessageEntityTypeNotSupported(type: String): Throwable("Entity type: $type not supported")
