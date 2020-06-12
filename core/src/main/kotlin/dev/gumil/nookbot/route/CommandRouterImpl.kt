package dev.gumil.nookbot.route

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.telegram.entities.Chat
import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.entities.User
import dev.gumil.nookbot.telegram.exceptions.CommandNotSupported
import dev.gumil.nookbot.telegram.exceptions.MessageEntityTypeNotSupported
import dev.gumil.nookbot.telegram.exceptions.NoContentException
import dev.gumil.nookbot.telegram.extractCommand
import dev.gumil.nookbot.service.OrdersService
import org.slf4j.LoggerFactory

internal class CommandRouterImpl(
    private val ordersService: OrdersService
): CommandRouter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun route(update: Update) {
        // Ignore updates with no message
        update.message ?: return

        // Ignore updates with unknown user
        update.message.from ?: return

        // Only support group messages
        if (update.message.chat.type != Chat.Type.GROUP) return

        val text = update.message.text
        val chatId = update.message.chat.id
        update.message.entities?.forEach { messageEntity ->
            if (text != null) {
                val (command, content) = try {
                    messageEntity.extractCommand(text)
                } catch (e: MessageEntityTypeNotSupported) {
                    logger.info("Ignoring update: ${e.message}")
                    return
                } catch (e: CommandNotSupported) {
                    logger.info("Ignoring update: ${e.message}")
                    return
                } catch (e: NoContentException) {
                    logger.info("Ignoring update: ${e.message}")
                    return
                }

                when (command) {
                    Command.ORDER -> order(chatId, update.updateId, content, update.message.from)
                    Command.TAKE -> TODO()
                    Command.CANCEL -> TODO()
                    Command.SENT -> TODO()
                    Command.LIST -> TODO()
                }
            }
        }
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
