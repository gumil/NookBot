package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.telegram.entities.InlineKeyboardButton
import dev.gumil.nookbot.telegram.entities.InlineKeyboardMarkup
import dev.gumil.nookbot.localization.Localization
import dev.gumil.nookbot.repository.OrderId
import dev.gumil.nookbot.repository.OrdersRepository
import dev.gumil.nookbot.route.Command
import dev.gumil.nookbot.telegram.TelegramApi
import dev.gumil.nookbot.telegram.request.EditMessageRequest
import dev.gumil.nookbot.telegram.request.SendMessageRequest
import org.slf4j.LoggerFactory

internal class TelegramOrdersService(
    private val repository: OrdersRepository,
    private val telegramApi: TelegramApi
) : OrdersService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun saveOrder(chatId: Long, order: Order) {
        repository.save(chatId, order)
        telegramApi.sendMessage(
            SendMessageRequest(
                chatId.toString(),
                getOrderPlacedText(order),
                getTakeOrderMarkup(order)
            )
        )
    }

    override suspend fun takeOrder(chatId: Long, messageId: Long, orderId: Long, seller: Resident) {
        val order = repository.getOrder(chatId, OrderId(orderId))

        if (order == null) {
            logger.info("Order not found")
            return
        }

        val hasPendingOrder = repository.hasPendingOrder(chatId, seller)

        if (hasPendingOrder) {
            telegramApi.sendMessage(
                SendMessageRequest(
                    chatId.toString(),
                    String.format(
                        Localization.userHasPendingOrder,
                        seller.name
                    )
                )
            )
            return
        }

        repository.save(chatId, order.copy(seller = seller))
        telegramApi.editMessageMarkUp(EditMessageRequest(chatId.toString(), messageId))
        telegramApi.sendMessage(
            SendMessageRequest(
                chatId.toString(),
                String.format(
                    Localization.orderTaken,
                    seller.name
                )
            )
        )
    }

    override suspend fun listOrder(chatId: Long) {
        val orders = repository.getOrders(chatId)

        if (orders.isNullOrEmpty()) {
            telegramApi.sendMessage(SendMessageRequest(chatId.toString(), Localization.noPendingOrders))
            return
        }

        val builder = StringBuilder()

        orders.forEach { order ->
            if (order.seller != null) {
                builder.append(String.format(
                    Localization.listOrderWithSellerItemBuyer,
                    order.seller.name,
                    order.name,
                    order.buyer.name
                ))
            } else {
                builder.append(
                    getOrderPlacedText(order)
                )
            }
            builder.append("\n")
        }
        telegramApi.sendMessage(SendMessageRequest(chatId.toString(), builder.toString()))
    }

    private fun getOrderPlacedText(order: Order): String {
        return String.format(
            Localization.orderPlaced,
            order.buyer.name,
            order.name
        )
    }

    private fun getTakeOrderMarkup(order: Order): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            listOf(
                listOf(
                    InlineKeyboardButton(
                        Localization.takeOrder,
                        "/${Command.TAKE.name} ${order.id}"
                    )
                )
            )
        )
    }
}
