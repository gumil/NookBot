package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.telegram.entities.InlineKeyboardButton
import dev.gumil.nookbot.telegram.entities.InlineKeyboardMarkup
import dev.gumil.nookbot.localization.Localization
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

    override suspend fun saveOrder(id: Long, order: Order) {
        repository.save(id, order)
        telegramApi.sendMessage(
            SendMessageRequest(
                id.toString(),
                String.format(
                    Localization.orderPlaced,
                    order.buyer.name,
                    order.name
                ),
                getTakeOrderMarkup(order)
            )
        )
    }

    override suspend fun takeOrder(id: Long, messageId: Long, orderId: Long, seller: Resident) {
        val order = repository.getOrder(id, orderId)

        if (order == null) {
            logger.info("Order not found")
            return
        }

        repository.save(id, order.copy(seller = seller))
        telegramApi.editMessageMarkUp(EditMessageRequest(id.toString(), messageId))
        telegramApi.sendMessage(
            SendMessageRequest(
                id.toString(),
                String.format(
                    Localization.orderTaken,
                    seller.name
                )
            )
        )
    }

    override suspend fun listOrder(id: Long) {
        val orders = repository.getOrders(id)

        if (orders.isNullOrEmpty()) {
            telegramApi.sendMessage(SendMessageRequest(id.toString(), Localization.noPendingOrders))
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
                    String.format(
                        Localization.orderPlaced,
                        order.buyer.name,
                        order.name
                    )
                )
            }
            builder.append("\n")
        }
        telegramApi.sendMessage(SendMessageRequest(id.toString(), builder.toString()))
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
