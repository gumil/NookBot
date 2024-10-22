package dev.gumil.nookbot.telegram.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(

    @SerialName("message_id")
    val messageId: Long,

    @SerialName("from")
    val from: User? = null,

    @SerialName("date")
    val date: Long,

    @SerialName("chat")
    val chat: Chat,

    @SerialName("entities")
    val entities: List<MessageEntity>? = null,

    @SerialName("text")
    val text: String? = null,

    @SerialName("reply_markup")
    val replyMarkup: InlineKeyboardMarkup? = null
)
