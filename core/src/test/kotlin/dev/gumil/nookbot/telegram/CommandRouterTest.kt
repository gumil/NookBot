package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.UpdateEntityFactory
import dev.gumil.nookbot.telegram.entities.CallbackQuery
import dev.gumil.nookbot.telegram.entities.Chat
import dev.gumil.nookbot.telegram.entities.Message
import dev.gumil.nookbot.telegram.entities.MessageEntity
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.entities.User
import dev.gumil.nookbot.telegram.exceptions.CommandParsingError
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CommandRouterTest {

    private val commandRouter = FakeCommandRouter()

    @Test
    fun `calling update should call update with extracted commands`() = runBlocking {
        val command = Random.nextInt().toString()
        val name = Random.nextInt().toString()
        val update = UpdateEntityFactory.getUpdate(command, name)

        commandRouter.route(update)

        commandRouter.verifyRouted(update, command, name)
    }

    @Test
    fun `handle MessageEntityTypeNotSupported error`() = runBlocking {
        val update = Update(
            Random.nextLong(),
            Message(
                Random.nextLong(),
                User(
                    Random.nextLong(),
                    false,
                    Random.nextDouble().toString()
                ),
                Random.nextLong(),
                Chat(
                    Random.nextLong(),
                    Chat.Type.GROUP
                ),
                listOf(
                    MessageEntity(
                        0,
                        6,
                        Random.nextDouble().toString()
                    )
                ),
                Random.nextDouble().toString()
            )
        )

        commandRouter.route(update)

        commandRouter.verifyError<CommandParsingError.MessageEntityTypeNotSupported>()
    }

    @Test
    fun `handle NoContent error`() = runBlocking {
        val command = Random.nextDouble().toString()
        val update = UpdateEntityFactory.getUpdate(command, "")

        commandRouter.route(update)

        commandRouter.verifyError<CommandParsingError.NoContent>()
    }

    @Test
    fun `handle callback query when text is null`() = runBlocking {
        val update = getUpdateCallbackQuery(null)

        commandRouter.route(update)

        commandRouter.verifyError<CommandParsingError.NoContent>()
    }

    @Test
    fun `handle callback query when text is empty`() = runBlocking {
        val update = getUpdateCallbackQuery("")

        commandRouter.route(update)

        commandRouter.verifyError<CommandParsingError.NoContent>()
    }

    @Test
    fun `handle callback query when text is unsupported`() = runBlocking {
        val data = Random.nextDouble().toString()
        val update = getUpdateCallbackQuery(data)

        commandRouter.route(update)

        commandRouter.verifyError<CommandParsingError.UnsupportedContent>()
    }

    @Test
    fun `handle callback query when text does not contain content`() = runBlocking {
        val update = getUpdateCallbackQuery("/take")

        commandRouter.route(update)

        commandRouter.verifyError<CommandParsingError.NoContent>()
    }

    @Test
    fun `handle callback query should call update with extracted commands`() = runBlocking {
        val command = Random.nextLong().toString()
        val content = Random.nextLong().toString()
        val fromCallbackQuery = User(
            Random.nextLong(),
            Random.nextBoolean(),
            Random.nextLong().toString()
        )
        val fromMessage = User(
            Random.nextLong(),
            Random.nextBoolean(),
            Random.nextLong().toString()
        )
        val messageId = Random.nextLong()
        val chat = Chat(
            Random.nextLong(),
            Chat.Type.GROUP
        )
        val date = Random.nextLong()
        val data = "/$command $content"
        val update = Update(
            Random.nextLong(),
            callbackQuery = CallbackQuery(
                Random.nextLong().toString(),
                fromCallbackQuery,
                Message(
                    messageId,
                    fromMessage,
                    date,
                    chat
                ),
                data = data
            )
        )
        val expectedUpdate = Update(
            update.updateId,
            Message(
                messageId,
                fromCallbackQuery,
                date,
                chat,
                text = data
            ),
            null
        )

        commandRouter.route(update)

        commandRouter.verifyRouted(expectedUpdate, command, content)
    }

    private fun getUpdateCallbackQuery(data: String?): Update {
        return Update(
            Random.nextLong(),
            callbackQuery = CallbackQuery(
                Random.nextLong().toString(),
                User(
                    Random.nextLong(),
                    false,
                    "hello world"
                ),
                data = data
            )
        )
    }

    @AfterEach
    fun tearDown() {
        commandRouter.tearDown()
    }
}