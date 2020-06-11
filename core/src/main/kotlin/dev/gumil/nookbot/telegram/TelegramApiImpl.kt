package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.entities.telegram.Message
import dev.gumil.nookbot.entities.telegram.Update
import dev.gumil.nookbot.telegram.request.SendMessageRequest
import dev.gumil.nookbot.telegram.response.HttpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders

internal class TelegramApiImpl(
    private val httpClient: HttpClient
): TelegramApi {

    private val token = System.getenv("NOOK_BOT")
    private val baseUrl = "https://api.telegram.org/bot$token/"
    private val getUpdates = "getUpdates"
    private val sendMessage = "sendMessage"

    override suspend fun getUpdates(offset: Long, timeout: Int): List<Update> {
        val urlString = baseUrl + getUpdates
        val query = "?offset=$offset" +
                "&timeout=$timeout" +
                "&allowed_updates=[message]"
        val httpResponse = httpClient.get<HttpResponse<List<Update>>>(
            urlString + query
        )
        return httpResponse.result
    }

    override suspend fun sendMessage(sendMessageRequest: SendMessageRequest): Message {
        val url = baseUrl + sendMessage
        val httpResponse = httpClient.post<HttpResponse<Message>>(url) {
            header(HttpHeaders.ContentType, "application/json")
            body = sendMessageRequest
        }
        return httpResponse.result
    }
}
