package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.Component
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.utils.TestMockEngine
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class TelegramApiImplTest {
    private val mockEngine = TestMockEngine()

    private val client = Component.provideHttpClient(mockEngine.get())

    private val telegramApiImpl = TelegramApiImpl(client)

    @ParameterizedTest
    @MethodSource("provideGetUpdatesArgs")
    fun `getUpdates returns list of updates from bot command`(file: String, expected: List<Update>) = runBlocking {
        mockEngine.enqueueResponseFromFile(file)

        val actual = telegramApiImpl.getUpdates(1, 1)

        assertEquals(expected, actual)
    }

    @Test
    fun `sendMessage returns message sent`() = runBlocking {
        mockEngine.enqueueResponseFromFile("sendMessage.json")

        val actual = telegramApiImpl.sendMessage(TelegramApiFactory.sendMessageRequest())

        assertEquals(TelegramApiFactory.getMessageResponse(), actual)
    }

    @Test
    fun `editMessage returns message edited`() = runBlocking {
        mockEngine.enqueueResponseFromFile("editMessageMarkup.json")

        val actual = telegramApiImpl.sendMessage(TelegramApiFactory.sendMessageRequest())

        assertEquals(TelegramApiFactory.getMessageResponse(null), actual)
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun provideGetUpdatesArgs(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("getUpdates/botCommand.json", TelegramApiFactory.getUpdatesBotCommand()),
                Arguments.of("getUpdates/groupChatCreated.json", TelegramApiFactory.getUpdatesGroupChatCreated()),
                Arguments.of("getUpdates/private.json", TelegramApiFactory.getUpdatesPrivate())
            )
        }
    }
}
