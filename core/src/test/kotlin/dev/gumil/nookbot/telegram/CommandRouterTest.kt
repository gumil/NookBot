package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.UpdateEntityFactory
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

    @AfterEach
    fun tearDown() {
        commandRouter.tearDown()
    }
}