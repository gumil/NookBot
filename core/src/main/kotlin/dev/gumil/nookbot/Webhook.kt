package dev.gumil.nookbot

import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.response.HttpResponse
import io.kotless.dsl.lang.KotlessContext
import io.kotless.dsl.lang.http.Post
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val router = Component.provideCommandRouter()
private val json = Component.provideJsonConfiguration()

@Post("/")
@Suppress("unused")
fun webhook(): String = runBlocking {
    val request = KotlessContext.HTTP.request
    val body = request.body ?: return@runBlocking "ignored"
    val message = String(body)
    Log.info(message)
    val update = json.parse(Update.serializer(), message)
    router.route(update)
    return@runBlocking "success"
}

object Log: Logger by LoggerFactory.getLogger(Log::class.java)
