package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.Component
import dev.gumil.nookbot.entities.Update
import dev.gumil.nookbot.readFromFile
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class TelegramApiImplTest {

    private val client = Component.provideHttpClient(MockEngine {
        respond(httpContent, headers = headersOf(HttpHeaders.ContentType, "application/json"))
    })

    private val telegramApiImpl = TelegramApiImpl(client)

    private lateinit var httpContent: String

    @ParameterizedTest
    @MethodSource("provideGetUpdatesArgs")
    fun `getUpdates returns list of updates from bot command`(file: String, expected: List<Update>) = runBlocking {

        httpContent = readFromFile(file)

        val actual = telegramApiImpl.getUpdates(1, 1)

        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun provideGetUpdatesArgs(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("getUpdates/botCommand.json", TelegramApiFactory.getUpdatesBotCommand()),
                Arguments.of("getUpdates/groupChatCreated.json", TelegramApiFactory.getUpdatesGroupChatCreated()),
                Arguments.of("getUpdates/private.json", TelegramApiFactory.getUpdatesPrivate())
            )
        }
    }
}
