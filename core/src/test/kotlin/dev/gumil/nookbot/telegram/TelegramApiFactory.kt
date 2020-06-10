package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.BOT_COMMAND
import dev.gumil.nookbot.entities.Chat
import dev.gumil.nookbot.entities.Message
import dev.gumil.nookbot.entities.MessageEntity
import dev.gumil.nookbot.entities.Update
import dev.gumil.nookbot.entities.User

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

    private fun getUser(): User {
        return User(
            123456,
            false,
            "firstname",
            "username",
            "en"
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