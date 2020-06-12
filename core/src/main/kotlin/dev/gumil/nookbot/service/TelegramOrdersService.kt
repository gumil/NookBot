package dev.gumil.nookbot.service

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.telegram.entities.InlineKeyboardButton
import dev.gumil.nookbot.telegram.entities.InlineKeyboardMarkup
import dev.gumil.nookbot.localization.Localization
import dev.gumil.nookbot.repository.OrdersRepository
import dev.gumil.nookbot.route.Command
import dev.gumil.nookbot.telegram.TelegramApi
import dev.gumil.nookbot.telegram.request.SendMessageRequest

internal class TelegramOrdersService(
    private val repository: OrdersRepository,
    private val telegramApi: TelegramApi
) : OrdersService {

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
