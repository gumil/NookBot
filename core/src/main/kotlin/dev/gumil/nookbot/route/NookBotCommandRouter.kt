package dev.gumil.nookbot.route

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.service.OrdersService
import dev.gumil.nookbot.telegram.CommandRouter
import dev.gumil.nookbot.telegram.entities.Chat
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.entities.User
import dev.gumil.nookbot.telegram.exceptions.CommandParsingError
import org.slf4j.LoggerFactory

internal class NookBotCommandRouter(
    private val ordersService: OrdersService
): CommandRouter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun route(update: Update, command: String, content: String) {
        // Ignore updates with no message
        update.message ?: return

        // Ignore updates with unknown user
        update.message.from ?: return

        // Only support group messages
        if (update.message.chat.type != Chat.Type.GROUP) return

        val nookCommand = try {
            Command.valueOf(command.toUpperCase())
        } catch (e: IllegalArgumentException) {
            logger.info("Ignoring update with $command")
        }

        val chatId = update.message.chat.id

        when (nookCommand) {
            Command.ORDER -> order(chatId, update.updateId, content, update.message.from)
            Command.TAKE -> TODO()
            Command.CANCEL -> TODO()
            Command.SENT -> TODO()
            Command.LIST -> TODO()
        }
    }

    override fun onParsingError(error: CommandParsingError) {
        logger.info("Ignoring update: ${error.message}")
    }

    private suspend fun order(chatId: Long, orderId: Long, content: String, user: User) {
        ordersService.saveOrder(
            chatId,
            Order(
                id = orderId,
                name = content,
                buyer = Resident(user.id, user.username ?: user.firstName)
            )
        )
    }
}
