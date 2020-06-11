package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.entities.HttpResponse
import dev.gumil.nookbot.entities.Update
import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal class TelegramApiImpl(
    private val httpClient: HttpClient
): TelegramApi {

    private val token = System.getenv("NOOK_BOT")
    private val baseUrl = "https://api.telegram.org/bot$token/"
    private val getUpdates = "getUpdates"

    override suspend fun getUpdates(offset: Int, timeout: Int): List<Update> {
        val urlString = baseUrl + getUpdates
        val query = "?offset=$offset" +
                "&timeout=$timeout" +
                "&allowed_updates=[message]"
        val httpResponse = httpClient.get<HttpResponse<List<Update>>>(
            urlString + query
        )
        return httpResponse.result
    }
}
