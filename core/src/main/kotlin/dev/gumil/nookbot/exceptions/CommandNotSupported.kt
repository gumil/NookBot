package dev.gumil.nookbot.exceptions

internal class CommandNotSupported(type: String): Throwable("Command type: $type not supported")
