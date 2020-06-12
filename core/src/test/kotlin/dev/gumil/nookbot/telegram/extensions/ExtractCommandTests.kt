package dev.gumil.nookbot.telegram.extensions

import dev.gumil.nookbot.telegram.BOT_COMMAND
import dev.gumil.nookbot.telegram.entities.MessageEntity
import dev.gumil.nookbot.telegram.exceptions.CommandNotSupported
import dev.gumil.nookbot.telegram.exceptions.MessageEntityTypeNotSupported
import dev.gumil.nookbot.telegram.exceptions.NoContentException
import dev.gumil.nookbot.telegram.extractCommand
import dev.gumil.nookbot.route.Command
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

@DisplayName("Tests for extractCommand")
internal class ExtractCommandTests {
    @Test
    fun `throws MessageEntityTypeNotSupported on invalid type`() {
        val type = Random.nextDouble().toString()
        val messageEntity = MessageEntity(0, 0, type)

        assertThrows<MessageEntityTypeNotSupported> {
            messageEntity.extractCommand("")
        }
    }

    @Test
    fun `returns CommandContent`() {
        val type = BOT_COMMAND
        val messageEntity = MessageEntity(0, 6, type)

        val actual = messageEntity.extractCommand("/order tinapay")

        val expected = Command.ORDER to "tinapay"

        assertEquals(expected, actual)
    }

    @Test
    fun `returns CommandContent always lowercased`() {
        val type = BOT_COMMAND
        val messageEntity = MessageEntity(0, 6, type)

        val actual = messageEntity.extractCommand("/OrDeR TINAPAY")

        val expected = Command.ORDER to "tinapay"

        assertEquals(expected, actual)
    }

    @Test
    fun `ignores characters after @ symbol`() {
        val type = BOT_COMMAND
        val command = Command.ORDER
        val botName = Random.nextDouble().toString()
        val text = Random.nextDouble().toString()
        val commandCall = "/$command@$botName"
        val messageEntity =
            MessageEntity(0, commandCall.length, type)

        val actual = messageEntity.extractCommand("$commandCall $text")

        val expected = command to text

        assertEquals(expected, actual)
    }

    @Test
    fun `thows exception for unsupported command`() {
        val type = BOT_COMMAND
        val command = Random.nextDouble().toString()
        val botName = Random.nextDouble().toString()
        val text = Random.nextDouble().toString()
        val commandCall = "/$command@$botName"
        val messageEntity =
            MessageEntity(0, commandCall.length, type)

        assertThrows<CommandNotSupported> {
            messageEntity.extractCommand("$commandCall $text")
        }
    }

    @Test
    fun `thows exception for command with no content`() {
        val type = BOT_COMMAND
        val command = Random.nextDouble().toString()
        val botName = Random.nextDouble().toString()
        val commandCall = "/$command@$botName"
        val messageEntity =
            MessageEntity(0, commandCall.length, type)

        assertThrows<NoContentException> {
            messageEntity.extractCommand(commandCall)
        }
    }
}
