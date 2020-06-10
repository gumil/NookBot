package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.entities.HttpResponse
import dev.gumil.nookbot.entities.Update
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

internal class TelegramApiImpl(
    private val httpClient: HttpClient,
    private val json: Json = Json(JsonConfiguration(
        ignoreUnknownKeys = true
    ))
): TelegramApi {

    private val token = System.getenv("NOOK_BOT")
    private val baseUrl = "https://api.telegram.org/bot$token/"
    private val getUpdates = "getUpdates"

    override suspend fun getUpdates(): List<Update> {
        val httpResponse = httpClient.get<String>(baseUrl + getUpdates)
        val parsedHttpResponse = json.parse(
            HttpResponse.serializer(
                ListSerializer(Update.serializer())
            ), httpResponse
        )
        return parsedHttpResponse.result
    }
}
