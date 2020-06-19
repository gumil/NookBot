package dev.gumil.nookbot

import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.runBlocking

@Suppress("MagicNumber")
fun main() = runBlocking {
    val api = Component.provideTelegramApi()
    val router = Component.provideCommandRouter()
    val timeout = 10 // seconds
    var offset = 0L

    while (true) {
        try {
            api.getUpdates(offset, timeout).forEach { update ->
                router.route(update)
                offset = update.updateId + 1
            }
        } catch (e: ClientRequestException) {
            println(e.response)
            break
        }
    }
}
