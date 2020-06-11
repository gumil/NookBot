package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.BOT_COMMAND
import dev.gumil.nookbot.entities.telegram.Chat
import dev.gumil.nookbot.entities.telegram.InlineKeyboardButton
import dev.gumil.nookbot.entities.telegram.InlineKeyboardMarkup
import dev.gumil.nookbot.entities.telegram.Message
import dev.gumil.nookbot.entities.telegram.MessageEntity
import dev.gumil.nookbot.entities.telegram.Update
import dev.gumil.nookbot.entities.telegram.User
import dev.gumil.nookbot.telegram.request.SendMessageRequest

object TelegramApiFactory {

    fun getUpdatesBotCommand(): List<Update> {
        return listOf(
            Update(
                68874152,
                Message(
                    12,
                    getUser(),
                    1591725215,
                    getChatGroup(),
                    listOf(
                        MessageEntity(
                            0,
                            5,
                            BOT_COMMAND
                        )
                    ),
                    "/help"
                )
            )
        )
    }

    fun getUpdatesGroupChatCreated(): List<Update> {
        return listOf(
            Update(
                68874151,
                Message(
                    11,
                    getUser(),
                    1591724528,
                    getChatGroup()
                )
            )
        )
    }

    fun getUpdatesPrivate(): List<Update> {
        return listOf(
            Update(
                68874150,
                Message(
                    10,
                    getUser(),
                    1591724503,
                    getChatPrivate(),
                    text = "a"
                )
            )
        )
    }

    fun sendMessageRequest(): SendMessageRequest {
        return SendMessageRequest(
            "123456",
            "golf bag",
            getInlineKeyboardMarkup()
        )
    }

    fun sendMessageResponse(): Message {
        return Message(
            36,
            getBotUser(),
            1591898165,
            getChatGroup(),
            text = "golf bag",
            replyMarkup = getInlineKeyboardMarkup()
        )
    }

    private fun getInlineKeyboardMarkup(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            listOf(
                listOf(
                    InlineKeyboardButton(
                        "Take order",
                        "/take order"
                    )
                )
            )
        )
    }

    private fun getUser(): User {
        return User(
            123456,
            false,
            "firstname",
            "username",
            "en"
        )
    }

    private fun getBotUser(): User {
        return User(
            1080542137,
            true,
            "NookBot",
            "nookex_bot"
        )
    }

    private fun getChatGroup(): Chat {
        return Chat(
            123456,
            Chat.Type.GROUP,
            "testnookbot"
        )
    }

    private fun getChatPrivate(): Chat {
        return Chat(
            123456,
            Chat.Type.PRIVATE,
            firstName = "firstname",
            username = "username"
        )
    }
}