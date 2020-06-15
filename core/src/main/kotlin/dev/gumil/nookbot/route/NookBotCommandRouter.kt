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
        val message = update.message
        message ?: return

        // Ignore updates with unknown user
        message.from ?: return

        // Only support group messages
        if (message.chat.type != Chat.Type.GROUP) return

        val nookCommand = try {
            Command.valueOf(command.toUpperCase())
        } catch (e: IllegalArgumentException) {
            logger.info("Ignoring update with $command")
        }

        val orderId = update.updateId
        val chatId = message.chat.id
        val messageId = message.messageId

        when (nookCommand) {
            Command.ORDER -> order(chatId, orderId, content, message.from)
            Command.TAKE -> takeOrder(chatId, content.toLong(), messageId, message.from)
            Command.CANCEL -> TODO()
            Command.SENT -> markOrderSent(chatId, message.from)
            Command.LIST -> listOrders(chatId)
        }
    }

    override fun onParsingError(error: CommandParsingError) {
        logger.info("Ignoring update: ${error.message}")
    }

    private suspend fun order(chatId: Long, orderId: Long, content: String, user: User) {
        //ignore blank content
        if (content.isBlank()) return

        ordersService.saveOrder(
            chatId,
            Order(
                id = orderId,
                name = content,
                buyer = Resident(user.id, user.username ?: user.firstName)
            )
        )
    }

    private suspend fun takeOrder(chatId: Long, orderId: Long, messageId: Long, user: User) {
        ordersService.takeOrder(
            chatId,
            messageId,
            orderId,
            Resident(
                user.id,
                user.username ?: user.firstName
            )
        )
    }

    private suspend fun markOrderSent(chatId: Long, user: User) {
        ordersService.markOrderSent(
            chatId,
            Resident(
                user.id,
                user.username ?: user.firstName
            )
        )
    }

    private suspend fun listOrders(chatId: Long) {
        ordersService.listOrder(chatId)
    }
}
