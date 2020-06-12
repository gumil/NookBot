package dev.gumil.nookbot.telegram.exceptions

internal class CommandNotSupported(type: String): Throwable("Command type: $type not supported")
