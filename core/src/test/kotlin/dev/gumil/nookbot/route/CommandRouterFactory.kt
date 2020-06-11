package dev.gumil.nookbot.route

import dev.gumil.nookbot.BOT_COMMAND
import dev.gumil.nookbot.entities.telegram.Chat
import dev.gumil.nookbot.entities.telegram.Message
import dev.gumil.nookbot.entities.telegram.MessageEntity
import dev.gumil.nookbot.entities.telegram.Update
import dev.gumil.nookbot.entities.telegram.User
import kotlin.random.Random

internal object CommandRouterFactory {

    fun getUpdateNoUser(): Update {
        return getUpdate()
    }

    fun getUpdateForOrder(name: String): Update {
        return getUpdate("order", name, getUser())
    }

    fun getUpdatePrivateChat(): Update {
        return getUpdate("order", null, getUser(), getChat(Chat.Type.PRIVATE))
    }

    private fun getUser(): User {
        return User(
            Random.nextLong(),
            false,
            Random.nextDouble().toString()
        )
    }

    private fun getUpdate(
        command: String? = null,
        name: String? = null,
        from: User? = null,
        chat: Chat = getChat()
    ): Update {
        val text = if (command != null && name != null) {
            "/$command $name"
        } else null
        return Update(
            Random.nextLong(),
            Message(
                Random.nextLong(),
                from,
                Random.nextLong(),
                chat,
                listOf(
                    MessageEntity(
                        0,
                        6,
                        BOT_COMMAND
                    )
                ),
                text
            )
        )
    }

    private fun getChat(type: Chat.Type = Chat.Type.GROUP): Chat {
        return Chat(
            Random.nextLong(),
            type
        )
    }
}
