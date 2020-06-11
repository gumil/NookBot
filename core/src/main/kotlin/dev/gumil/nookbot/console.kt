package dev.gumil.nookbot

import dev.gumil.nookbot.telegram.TelegramApiImpl
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val api = TelegramApiImpl(Component.provideHttpClient())
    val timeout = 5 //seconds
    var offset = 0

    while (true) {
        api.getUpdates(offset, timeout).forEach {
            println(it)
            offset = it.updateId + 1
        }
    }
}
