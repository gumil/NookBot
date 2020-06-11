package dev.gumil.nookbot.entities.telegram

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(

    @SerialName("message_id")
    val messageId: Long,

    @SerialName("from")
    val from: User? = null,

    @SerialName("date")
    val date: Int,

    @SerialName("chat")
    val chat: Chat,

    @SerialName("entities")
    val entities: List<MessageEntity>? = null,

    @SerialName("text")
    val text: String? = null
)