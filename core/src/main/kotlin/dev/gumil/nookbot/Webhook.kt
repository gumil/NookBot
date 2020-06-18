package dev.gumil.nookbot

import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.response.HttpResponse
import io.kotless.dsl.lang.KotlessContext
import io.kotless.dsl.lang.http.Post
import kotlinx.coroutines.runBlocking

private val router = Component.provideCommandRouter()
private val json = Component.provideJsonConfiguration()

@Post("/")
fun webhook(): String = runBlocking {
    val request = KotlessContext.HTTP.request
    val body = request.body ?: return@runBlocking "ignored"
    val update = json.parse(HttpResponse.serializer(Update.serializer()), String(body))
    router.route(update.result)
    return@runBlocking "success"
}
