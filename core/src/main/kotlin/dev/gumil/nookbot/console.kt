package dev.gumil.nookbot

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val api = Component.provideTelegramApi()
    val router = Component.provideCommandRouter()
    val timeout = 5 //seconds
    var offset = 0L

    while (true) {
        api.getUpdates(offset, timeout).forEach { update ->
            router.route(update)
            offset = update.updateId + 1
        }
    }
}
